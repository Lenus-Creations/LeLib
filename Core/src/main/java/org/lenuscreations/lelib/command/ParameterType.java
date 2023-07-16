package org.lenuscreations.lelib.command;

import java.util.ArrayList;
import java.util.List;

public interface ParameterType<T, E> {

    T parse(E executor, String target);

    default List<String> completer(E executor, String source) {
        return new ArrayList<>();
    }

}
