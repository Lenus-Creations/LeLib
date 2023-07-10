package org.lenuscreations.letlib.file.value;

public interface ConfigValue<T> {

    T parse(Object obj);

}
