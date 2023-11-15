package org.lenuscreations.lelib.arguments;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgumentHandler {

    private final List<ArgumentNode> registeredArguments = new ArrayList<>();

    @SneakyThrows
    public void process(String... args) {
        for (int i = 0; i < args.length; i++) {
            for (ArgumentNode node : registeredArguments) {
                String arg = args[i];
                if (arg.equals("-" + (node.getValues().length == 0 ? "-" : "") + node.getName()) ||
                        Lists.newArrayList(node.getAliases()).contains(arg.replaceFirst("-" + (node.getValues().length == 0 ? "-" : ""), ""))) {
                    List<Object> values = new ArrayList<>();
                    for (ArgValue value : node.getValues()) {
                        switch (value.type().getSimpleName()) {
                            case "String":
                                String val = args[++i];
                                if (val.startsWith("\"")) {
                                    val = val.substring(1);

                                    while (!val.endsWith("\"")) {
                                        val += " " + args[++i];
                                    }

                                    val = val.substring(0, val.length() - 1);
                                }

                                values.add(val);
                                break;
                            case "int":
                            case "Integer":
                                values.add(Integer.parseInt(args[++i]));
                                break;
                            case "double":
                            case "Double":
                                values.add(Double.parseDouble(args[++i]));
                                break;
                            case "float":
                            case "Float":
                                values.add(Float.parseFloat(args[++i]));
                                break;
                            case "long":
                            case "Long":
                                values.add(Long.parseLong(args[++i]));
                                break;
                            case "boolean":
                            case "Boolean":
                                values.add(Boolean.parseBoolean(args[++i]));
                                break;
                            default:
                                throw new IllegalArgumentException("Unknown type: " + value.type().getSimpleName());
                        }
                    }

                    try {
                        node.getMethod().invoke(node.getInstance(), values.toArray());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void register(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Argument.class)) {
                Argument argument = method.getAnnotation(Argument.class);
                ArgumentNode node = new ArgumentNode(argument, method);
                registeredArguments.add(node);
            }
        }
    }

}
