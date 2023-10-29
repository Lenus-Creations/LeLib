package org.lenuscreations.velocity.command;

import com.google.common.collect.Lists;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.Scheduler;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.lenuscreations.lelib.command.Arg;
import org.lenuscreations.lelib.command.Command;
import org.lenuscreations.lelib.command.Flag;
import org.lenuscreations.lelib.command.FlagValue;
import org.lenuscreations.velocity.VAbstractPlugin;
import org.lenuscreations.velocity.command.arguments.Argument;
import org.lenuscreations.velocity.command.arguments.ArgumentParser;
import org.lenuscreations.velocity.command.arguments.ArgumentType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CommandNode {

    private final String name;
    private final String description;
    private final String[] aliases;
    private final String permission;
    private boolean playerOnly;
    private boolean async;

    private Class<?> clazz;
    private final Method method;
    private final Object instance;

    private Map<String, CommandNode> children;

    public CommandNode(Method method, Command ann, Object instance) {
        this.name = ann.name();
        this.description = ann.description();
        this.aliases = ann.aliases();
        this.permission = ann.permission();
        this.async = ann.async();

        this.clazz = method.getDeclaringClass();
        this.method = method;
        this.instance = instance;

        this.children = new HashMap<>();
    }

    public void execute(CommandSource sender, String[] args) throws InvocationTargetException, IllegalAccessException {
        List<Object> parameterArgs = new ArrayList<>();
        List<Parameter> parameters = new ArrayList<>();

        if (!canAccess(sender)) {
            sender.sendMessage(CommandHandler.NO_PERMISSION_MESSAGE);
            return;
        }

        Class<?> executorClass = method.getParameters()[0].getType();
        if (executorClass == Player.class) playerOnly = true;
        else if (executorClass == CommandSource.class) {
            playerOnly = false;
        }

        if (!(sender instanceof Player) && playerOnly) {
            sender.sendMessage(CommandHandler.PLAYER_ONLY_MESSAGE);
            return;
        }

        List<CommandNode> children = getChild(args, true);
        if (!children.isEmpty()) {
            CommandNode child = children.get(0);

            List<String> newArgs = Lists.newArrayList(args);
            newArgs.removeAll(
                    Lists.newArrayList(child.getName().split(" "))
            );

            child.execute(sender, newArgs.toArray(new String[0]));
            return;
        }

        for (int i = 1; i < method.getParameters().length; i++) {
            parameters.add(method.getParameters()[i]);
        }

        parameterArgs.add(sender);

        ArgumentParser parser = new ArgumentParser(sender);
        Map<Integer, Argument> arguments = parser.parse(args, parameters.toArray(new Parameter[0]));

        if (arguments.size() < method.getParameterCount() - 1 || arguments.size() > method.getParameterCount() - 1) {
            this.sendUsageMessage(sender, method.getParameters());
            return;
        }

        if (arguments.values().stream().anyMatch(a -> a.getValue() == null && a.getType() != ArgumentType.FLAG_VALUE)) {
            this.sendUsageMessage(sender, method.getParameters());
            return;
        }

        for (int i = 0; i < arguments.size(); i++) {
            parameterArgs.add(arguments.get(i).getValue());
        }

        if (async) {
            new Thread(() -> {
                try {
                    method.invoke(instance, parameterArgs.toArray());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        } else {
            method.invoke(instance, parameterArgs.toArray());
        }
    }

    public List<String> suggest(CommandSource sender, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (!canAccess(sender)) return suggestions;

        List<CommandNode> children = getChild(args, false);
        if (!children.isEmpty()) {
            CommandNode child = children.get(0);

            List<String> newArgs = Lists.newArrayList(args);
            newArgs.removeAll(
                    Lists.newArrayList(child.getName().split(" "))
            );

            suggestions.addAll(child.suggest(sender, newArgs.toArray(new String[0])));
            return suggestions;
        }

        for (Map.Entry<String, CommandNode> entry : this.children.entrySet()) {
            if (entry.getKey().startsWith(String.join(" ", args))) {
                suggestions.add(entry.getKey());
            }
        }

        return suggestions;
    }

    protected boolean canAccess(CommandSource sender) {
        if (!(sender instanceof Player)) return true;
        if (permission.isEmpty()) return true;

        Player p = (Player) sender;
        if (p.hasPermission("*") || p.hasPermission("*.*")) return true;

        return p.hasPermission(permission);
    }

    private List<CommandNode> getChild(String[] args, boolean full) {
        List<CommandNode> possibleChildren = new ArrayList<>();
        List<String> argsList = Lists.newArrayList(args);

        if (full) {
            for (int i = argsList.size(); i > 0; i--) {
                String[] c = argsList.toArray(new String[0]);

                CommandNode childFound = children.get(String.join(" ", c));
                if (childFound != null) {
                    possibleChildren.add(childFound);
                    break;
                }

                argsList.remove((i - 1));
            }
        } else {
            for (Map.Entry<String, CommandNode> entry : children.entrySet()) {
                if (entry.getKey().startsWith(String.join(" ", args))) {
                    possibleChildren.add(entry.getValue());
                }
            }
        }

        return possibleChildren;
    }

    public void sendUsageMessage(CommandSource sender, Parameter[] parameters) {
        StringBuilder str = new StringBuilder("Usage: /" + getName() + " ");
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(Arg.class)) {
                Arg arg = parameter.getAnnotation(Arg.class);
                str.append((arg.defaultValue().isEmpty() ? "<" : "["))
                        .append(arg.name());
                if (arg.wildcard()) str.append("...");
                str.append((arg.defaultValue().isEmpty() ? "> " : "] "));
            } else if (parameter.isAnnotationPresent(Flag.class)) {
                Flag flag = parameter.getAnnotation(Flag.class);
                str.append("[-")
                        .append(flag.value())
                        .append("] ");
            } else if (parameter.isAnnotationPresent(FlagValue.class)) {
                FlagValue flagValue = parameter.getAnnotation(FlagValue.class);
                str
                        .append("[-")
                        .append(flagValue.flagName())
                        .append(" <")
                        .append(flagValue.valueName())
                        .append(">] ");
            }
        }

        sender.sendMessage(Component.text(str.toString()).color(NamedTextColor.RED));
    }

    public void addChild(String name, CommandNode node) {
        children.put(name, node);
    }
}