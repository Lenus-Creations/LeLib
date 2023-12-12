package org.lenuscreations.lelib.utils.reflection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;

@Data
@ToString
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LMethod {

    private final Method method;

    private final String name;
    private final String returnType;
    private final LParameter[] parameters;
    private final LModifier[] modifiers;

    private final Class<?> declaringClass;

    public boolean isReturnType(String type) {
        return this.returnType.equals(type);
    }

    public boolean isReturnType(Class<?> type) {
        return this.returnType.equals(type.getName());
    }

    public boolean isReturnType(LClass type) {
        return this.returnType.equals(type.getName());
    }

    public boolean hasModifier(LModifier modifier) {
        return Arrays.asList(this.modifiers).contains(modifier);
    }

    public Object invoke(Object instance, Object... args) {
        try {
            return this.method.invoke(instance, args);
        } catch (Exception e) {
            return null;
        }
    }

}
