package org.lenuscreations.lelib.file.value;

public interface ConfigValue<T> {

    T getValue();

    ConfigValue<T> parse(Object obj);

}
