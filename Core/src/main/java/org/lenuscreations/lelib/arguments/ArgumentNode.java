package org.lenuscreations.lelib.arguments;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class ArgumentNode {

    private final String name;
    private final String[] aliases;
    private final String description;
    private final boolean required;
    private final ArgValue[] values;

    private final Argument argument;
    private final Method method;
    private final Class<?> instance;

    public ArgumentNode(Argument argument, Method method) {
        this.argument = argument;
        this.method = method;
        this.instance = method.getDeclaringClass();

        this.name = argument.name();
        this.aliases = argument.aliases();
        this.description = argument.description();
        this.required = argument.required();
        this.values = argument.values();
    }

}
