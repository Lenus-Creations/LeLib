package org.lenuscreations.lelib.bukkit.command.arguments;

import org.bukkit.command.CommandSender;
import org.lenuscreations.lelib.bukkit.command.CommandHandler;
import org.lenuscreations.lelib.command.Arg;
import org.lenuscreations.lelib.command.Flag;
import org.lenuscreations.lelib.command.FlagValue;
import org.lenuscreations.lelib.command.ParameterType;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.lenuscreations.lelib.utils.StringUtil.shift;

public class ArgumentParser {

    private final Map<Integer, Argument> args;
    private final CommandSender executor;

    public ArgumentParser(CommandSender executor) {
        this.args = new HashMap<>();
        this.executor = executor;
    }

    public Map<Integer, Argument> parse(String[] args, Parameter[] parameters) {

        int i = 0;
        for (Parameter param : parameters) {
            i++;
            if (args.length == 0 && i != parameters.length && (param.isAnnotationPresent(Arg.class)
                    && param.getDeclaredAnnotation(Arg.class).defaultValue().isEmpty()))
                continue;

            if (param.isAnnotationPresent(Arg.class)) args = this.parseArgs(param, args);
            else if (param.isAnnotationPresent(Flag.class)) args = this.parseFlags(param, args);
            else if (param.isAnnotationPresent(FlagValue.class)) args = this.parseFlagValues(param, args);
            else {
                throw new RuntimeException("Cannot find any annotation for '" + param.getName() + "'. Please use @Arg, @Flag or @FlagValue.");
            }
        }

        if (args.length > 0) {
            this.args.put(999, new Argument(ArgumentType.ARGUMENT, "unknown", args[0], null));
        }

        return this.args;
    }

    private String[] parseArgs(Parameter parameter, String[] args) {
        Arg arg = parameter.getAnnotation(Arg.class);
        if (arg.wildcard()) {
            if (args.length == 0) return new String[0];
            String value = String.join(" ", args);

            Argument argument = new Argument(ArgumentType.ARGUMENT, arg.name(), value, null);

            int size = this.args.size();
            this.args.put(size, argument);

            return new String[0];
        }

        String value;
        if (args.length == 0 && !arg.defaultValue().isEmpty()) value = arg.defaultValue();
        else if (args.length > 0) value = args[0];
        else return new String[0];

        ParameterType<?, CommandSender> parameterType = CommandHandler.getParameterTypes().get(parameter.getType());
        if (parameterType == null) throw new RuntimeException();

        Object valueObject = parameterType.parse(executor, value);
        Argument argument = new Argument(ArgumentType.ARGUMENT, arg.name(), valueObject, null);

        int size = this.args.size();
        this.args.put(size, argument);

        return shift(args);
    }

    private String[] parseFlags(Parameter parameter, String[] args) {
        if (!parameter.getType().toString().equalsIgnoreCase("boolean")) throw new RuntimeException("@Flag only supports booleans.");

        Flag flag = parameter.getAnnotation(Flag.class);
        String flagName = "-" + flag.value();

        boolean hasFlag = Arrays.asList(args).contains(flagName);

        Argument argument = new Argument(ArgumentType.FLAG, null, hasFlag, flag.value());
        int size = this.args.size();
        this.args.put(size, argument);
        return (hasFlag ? shift(args, flagName) : args);
    }

    private String[] parseFlagValues(Parameter parameter, String[] args) {
        FlagValue flagValue = parameter.getAnnotation(FlagValue.class);
        String flagName = "-" + flagValue.flagName();

        if (args.length == 0) {
            Argument argument = new Argument(ArgumentType.FLAG_VALUE, flagValue.valueName(), null, flagValue.flagName());
            int size = this.args.size();
            this.args.put(size, argument);

            return args;
        }

        if (args[0].equalsIgnoreCase(flagName)) {
            args = shift(args);
            if (args.length == 0) return new String[0];

            ParameterType<?, CommandSender> parameterType = CommandHandler.getParameterTypes().get(parameter.getType());
            if (parameterType == null) throw new RuntimeException();

            Object valueObject = parameterType.parse(executor, args[0]);
            Argument argument = new Argument(ArgumentType.FLAG_VALUE, flagValue.valueName(), valueObject, flagValue.flagName());

            int size = this.args.size();
            this.args.put(size, argument);
            return shift(args);
        }

        Argument argument = new Argument(ArgumentType.FLAG_VALUE, flagValue.valueName(), null, flagValue.flagName());
        int size = this.args.size();
        this.args.put(size, argument);

        return args;
    }

}
