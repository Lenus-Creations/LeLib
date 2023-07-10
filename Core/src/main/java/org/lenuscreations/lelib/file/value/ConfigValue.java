package org.lenuscreations.lelib.file.value;

public interface ConfigValue<T> {

    T parse(Object obj);

}
