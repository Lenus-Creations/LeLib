package org.lenuscreations.lelib.command.impl.parameters;

import org.lenuscreations.lelib.command.ParameterType;

import java.util.ArrayList;
import java.util.List;

public class StringParameter implements ParameterType<String, Object> {

    @Override
    public String parse(Object executor, String target) {
        return target;
    }

    @Override
    public List<String> completer(Object executor, String[] args) {
        return new ArrayList<>();
    }
}
