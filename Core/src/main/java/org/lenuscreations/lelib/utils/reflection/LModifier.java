package org.lenuscreations.lelib.utils.reflection;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Modifier;

@Getter
@AllArgsConstructor
public enum LModifier {

    PUBLIC(Modifier.PUBLIC),
    PRIVATE(Modifier.PRIVATE),
    PROTECTED(Modifier.PROTECTED),
    STATIC(Modifier.STATIC),
    FINAL(Modifier.FINAL),
    SYNCHRONIZED(Modifier.SYNCHRONIZED),
    VOLATILE(Modifier.VOLATILE),
    TRANSIENT(Modifier.TRANSIENT),
    NATIVE(Modifier.NATIVE),
    INTERFACE(Modifier.INTERFACE),
    ABSTRACT(Modifier.ABSTRACT),
    STRICT(Modifier.STRICT);

    private final int mask;

}
