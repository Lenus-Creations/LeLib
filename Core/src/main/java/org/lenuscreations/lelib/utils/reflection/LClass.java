package org.lenuscreations.lelib.utils.reflection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Data
@ToString
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LClass {

    private final Class<?> clazz;

    private final String name;
    private final String packageName;
    private final String simpleName;

    private final LClass superClass;
    private final LClass[] interfaces;

    private final LField[] fields;
    private final LMethod[] methods;

    @Nullable
    public LField getField(String name) {
        return Arrays.stream(this.fields).filter(f -> f.getName().equals(name)).findFirst().orElse(null);
    }

    @Nullable
    public LMethod getMethod(String name) {
        return Arrays.stream(this.methods).filter(m -> m.getName().equals(name)).findFirst().orElse(null);
    }

    public boolean isType(String type) {
        return this.name.equals(type);
    }

    public boolean isType(Class<?> type) {
        return this.name.equals(type.getName());
    }

    public boolean isType(LClass type) {
        return this.name.equals(type.getName());
    }

}
