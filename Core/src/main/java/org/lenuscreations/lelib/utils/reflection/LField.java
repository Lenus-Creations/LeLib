package org.lenuscreations.lelib.utils.reflection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

@Data
@ToString
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LField {

    private final Field field;

    private final String name;
    private final String type;
    private final LModifier[] modifiers;

    private final Class<?> declaringClass;
    private final Class<?> typeClass;

    public boolean isType(String type) {
        return this.type.equals(type);
    }

    public boolean isType(Class<?> type) {
        return this.typeClass.getName().equals(type.getName());
    }

    public boolean isType(LClass type) {
        return this.typeClass.getName().equals(type.getName());
    }

    public boolean hasModifier(LModifier modifier) {
        return Arrays.asList(this.modifiers).contains(modifier);
    }

    @Nullable
    public Object get(Object instance) {
        try {
            return this.field.get(instance);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public boolean set(Object instance, Object value) {
        try {
            this.field.set(instance, value);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

}
