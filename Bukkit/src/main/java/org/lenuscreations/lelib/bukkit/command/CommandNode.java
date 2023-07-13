package org.lenuscreations.lelib.bukkit.command;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.command.arguments.Argument;
import org.lenuscreations.lelib.bukkit.command.arguments.ArgumentParser;
import org.lenuscreations.lelib.bukkit.command.arguments.ArgumentType;
import org.lenuscreations.lelib.bukkit.utils.Util;
import org.lenuscreations.lelib.command.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

import static org.lenuscreations.lelib.utils.StringUtil.shift;

@Data
public class CommandNode {

    private final String name;
    private final String description;
    private final String[] aliases;
    private final String permission;
    private boolean playerOnly;
    private boolean consoleOnly;
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

    public void execute(CommandSender sender, String[] args) throws InvocationTargetException, IllegalAccessException {
        List<Object> parameterArgs = new ArrayList<>();
        List<Parameter> parameters = new ArrayList<>();

        if (!canAccess(sender)) {
            sender.sendMessage(Util.format(CommandHandler.NO_PERMISSION_MESSAGE));
            return;
        }

        Class<?> executorClass = method.getParameters()[0].getType();
        if (executorClass == ConsoleCommandSender.class) consoleOnly = true;
        else if (executorClass == Player.class) playerOnly = true;
        else if (executorClass == CommandSender.class) {
            consoleOnly = false;
            playerOnly = false;
        }

        if (!(sender instanceof Player) && playerOnly) {
            sender.sendMessage(Util.format(CommandHandler.PLAYER_ONLY_MESSAGE));
            return;
        }

        if (sender instanceof Player && consoleOnly) {
            sender.sendMessage(Util.format(CommandHandler.CONSOLE_ONLY_MESSAGE));
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

        parameterArgs.add((playerOnly ? (Player) sender : (consoleOnly ? (ConsoleCommandSender) sender : sender)));

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
            Bukkit.getScheduler().runTaskAsynchronously(AbstractPlugin.getInstance(), () -> {
                try {
                    method.invoke(instance, parameterArgs.toArray());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            method.invoke(instance, parameterArgs.toArray());
        }
    }

    public List<String> tabComplete(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();

        ParameterType<?, CommandSender> parameterType = null;
        if (args.length <= method.getParameterCount()) {
            parameterType = CommandHandler.getParameterTypes().get(method.getParameterTypes()[args.length - 1]);
        }

        if (parameterType != null) {
            String arg = args[args.length - 1];
            arguments.addAll(parameterType.completer(player, arg).stream().filter(a -> a.startsWith(arg)).collect(Collectors.toList()));
        }

        for (String childString : children.keySet()) {
            String[] split = childString.split(" ");

            List<CommandNode> nodes = getChild(split, false);
            for (CommandNode node : nodes) {
                if (args.length >= split.length) continue;

                String name = node.getName().split(" ")[args.length - 1];
                if (arguments.contains(name)) continue;

                if (StringUtils.startsWithIgnoreCase(name, args[args.length - 1])) {
                    arguments.add(name);
                }
            }
        }

        List<CommandNode> possibleChildren = getChild(args, true);
        if (!possibleChildren.isEmpty()) {
            CommandNode child = possibleChildren.get(0);
            return child.tabComplete(player, args);
        }

        return arguments;
    }

    protected boolean canAccess(CommandSender sender) {
        if (!(sender instanceof Player)) return true;
        if (permission.isEmpty()) return true;

        Player p = (Player) sender;
        if (permission.equalsIgnoreCase("op")) return p.isOp();

        if (p.isOp()) return true;
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

    public void sendUsageMessage(CommandSender sender, Parameter[] parameters) {
        StringBuilder str = new StringBuilder("&cUsage: /" + getName() + " ");
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(Arg.class)) {
                Arg arg = parameter.getAnnotation(Arg.class);
                str.append((arg.defaultValue().isEmpty() ? "<" : "["))
                        .append(arg.name())
                        .append((arg.defaultValue().isEmpty() ? "> " : "] "));
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

        sender.sendMessage(Util.format(str.toString()));
    }

    public void addChild(String name, CommandNode node) {
        children.put(name, node);
    }
}
