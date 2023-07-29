package org.lenuscreations.lelib.file.value;

public interface ConfigValue<T> {

    T getValue();

    T parse(Object obj);

}
