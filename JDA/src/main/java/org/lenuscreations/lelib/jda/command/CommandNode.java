package org.lenuscreations.lelib.jda.command;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.lenuscreations.lelib.command.Command;
import org.lenuscreations.lelib.jda.Node;
import org.lenuscreations.lelib.jda.command.arguments.Argument;
import org.lenuscreations.lelib.jda.command.arguments.ArgumentParser;
import org.lenuscreations.lelib.jda.command.arguments.ArgumentType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CommandNode implements Node<Member, Message, String[]> {

    private final String name;
    private final String description;
    private final String[] aliases;
    private final Permission permission;
    private boolean async;

    private Class<?> clazz;
    private final Method method;
    private final Object instance;

    private Map<String, CommandNode> children;

    public CommandNode(Method method, Command ann, Object instance) {
        this.name = ann.name();
        this.description = ann.description();
        this.aliases = ann.aliases();
        this.permission = Permission.valueOf(ann.permission());
        this.async = ann.async();

        this.clazz = method.getDeclaringClass();
        this.method = method;
        this.instance = instance;

        this.children = new HashMap<>();
    }

    @SneakyThrows
    @Override
    public void execute(Member member, String[] args, Message message) {
        List<Object> parameterArgs = new ArrayList<>();
        List<Parameter> parameters = new ArrayList<>();

        if (!canAccess(member)) {
            message.getChannel().sendMessage("").queue();
            return;
        }

        List<CommandNode> children = getChild(args, true);
        if (!children.isEmpty()) {
            CommandNode child = children.get(0);

            List<String> newArgs = Lists.newArrayList(args);
            newArgs.removeAll(
                    Lists.newArrayList(child.getName().split(" "))
            );

            child.execute(member, newArgs.toArray(new String[0]), message);
            return;
        }

        for (int i = 1; i < method.getParameters().length; i++) {
            parameters.add(method.getParameters()[i]);
        }

        parameterArgs.add(member);

        ArgumentParser parser = new ArgumentParser(method);
        Map<Integer, Argument> arguments = parser.parse(args, parameters.toArray(new Parameter[0]));

        if (arguments.size() < method.getParameterCount() - 1 || arguments.size() > method.getParameterCount() - 1) {
            //this.sendUsageMessage(member, method.getParameters());
            return;
        }

        if (arguments.values().stream().anyMatch(a -> a.getValue() == null && a.getType() != ArgumentType.FLAG_VALUE)) {
            //this.sendUsageMessage(member, method.getParameters());
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

    public boolean canAccess(Member member) {
        return member.isOwner() || member.hasPermission(permission);
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

}
