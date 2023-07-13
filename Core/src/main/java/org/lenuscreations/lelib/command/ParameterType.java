package org.lenuscreations.lelib.command;

import java.util.List;

public interface ParameterType<T, E> {

    T parse(E executor, String target);

    List<String> completer(E executor, String source);

}
