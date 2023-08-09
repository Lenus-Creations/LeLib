package org.lenuscreations.lelib.jda.command;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.Command.Option;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.lenuscreations.lelib.command.Arg;
import org.lenuscreations.lelib.command.Command;
import org.lenuscreations.lelib.command.Flag;
import org.lenuscreations.lelib.command.FlagValue;
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
import java.util.stream.Collectors;

@Data
public class CommandNode implements Node<Member, Message, Option[]> {

    private final String name;
    private final String description;
    private final String[] aliases;
    private final Permission permission;
    private boolean async;

    private Class<?> clazz;
    private final Method method;
    private final Object instance;
    private final Type type;

    private Map<String, CommandNode> children;

    public CommandNode(Method method, Command ann, Type type, Object instance) {
        this.name = ann.name();
        this.description = ann.description();
        this.aliases = ann.aliases();
        this.permission = Permission.valueOf(ann.permission());
        this.async = ann.async();

        this.clazz = method.getDeclaringClass();
        this.method = method;
        this.instance = instance;
        this.type = type;

        this.children = new HashMap<>();
    }

    @SneakyThrows
    @Override
    public void execute(Member member, Option[] options, Message message) {
        /*List<Object> parameterArgs = new ArrayList<>();
        List<Parameter> parameters = new ArrayList<>();

        if (!canAccess(member)) {
            message.getChannel().sendMessage("").queue();
            return;
        }

        List<CommandNode> children = getChild(options, true);
        if (!children.isEmpty()) {
            CommandNode child = children.get(0);

            List<String> newArgs = Lists.newArrayList(options);
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

        ArgumentParser parser = new ArgumentParser(member);
        Map<Integer, Argument> arguments = parser.parse(args, parameters.toArray(new Parameter[0]));

        if (arguments.size() < method.getParameterCount() - 1 || arguments.size() > method.getParameterCount() - 1) {
            this.sendUsageMessage(message, method.getParameters());
            return;
        }

        if (arguments.values().stream().anyMatch(a -> a.getValue() == null && a.getType() != ArgumentType.FLAG_VALUE)) {
            this.sendUsageMessage(message, method.getParameters());
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

    public void sendUsageMessage(Message message, Parameter[] parameters) {
        if (JDACommandHandler.USAGE_MESSAGE_OPTION == Options.NONE) return;

        StringBuilder str = new StringBuilder("&cUsage: /" + getName() + " ");
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(Arg.class)) {
                Arg arg = parameter.getAnnotation(Arg.class);
                str.append((arg.defaultValue().isEmpty() ? "<" : "["))
                        .append(arg.name());
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

        switch (JDACommandHandler.USAGE_MESSAGE_OPTION) {
            case MESSAGE:
                message.reply(JDACommandHandler.USAGE_MESSAGE).queue();
                break;
            case EMBED:
                message.replyEmbeds(JDACommandHandler.USAGE_MESSAGE_EMBED.build()).queue();
                break;
            case BOTH:
                message.reply(JDACommandHandler.USAGE_MESSAGE)
                        .addEmbeds(JDACommandHandler.USAGE_MESSAGE_EMBED.build())
                        .queue();
                break;
        }*/
    }

    public boolean canAccess(Member member) {
        return member.isOwner() || member.hasPermission(permission);
    }

    private List<CommandNode> getChild(Option[] options, boolean full) {
        List<CommandNode> possibleChildren = new ArrayList<>();
        // incorrect
        List<String> argsList = Lists.newArrayList(options).stream().map(Option::getName).collect(Collectors.toList());

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
                if (entry.getKey().startsWith(String.join(" ", argsList.toArray(new String[0])))) {
                    possibleChildren.add(entry.getValue());
                }
            }
        }

        return possibleChildren;
    }

}
