package org.lenuscreations.lelib.utils.reflection;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;

@UtilityClass
public class ReflectionUtil {

    public static LClass ofClass(Class<?> clazz) {
        if (clazz == null) return null;

        return new LClass(
                clazz,
                clazz.getName(),
                clazz.getPackage() == null ? null : clazz.getPackage().getName(),
                clazz.getSimpleName(),
                ofClass(clazz.getSuperclass()),
                Arrays.stream(clazz.getInterfaces()).map(ReflectionUtil::ofClass).toArray(LClass[]::new),
                Arrays.stream(clazz.getDeclaredFields()).map(ReflectionUtil::ofField).toArray(LField[]::new),
                Arrays.stream(clazz.getDeclaredMethods()).map(ReflectionUtil::ofMethod).toArray(LMethod[]::new)
        );
    }

    public static LField ofField(Field field) {
        if (field == null) return null;

        return new LField(
                field,
                field.getName(),
                field.getType().getName(),
                toModifiers(field.getModifiers()),
                field.getDeclaringClass(),
                field.getType()
        );
    }

    public static LMethod ofMethod(java.lang.reflect.Method method) {
        if (method == null) return null;

        return new LMethod(
                method,
                method.getName(),
                method.getReturnType().getName(),
                Arrays.stream(method.getParameters()).map(ReflectionUtil::ofParameter).toArray(LParameter[]::new),
                toModifiers(method.getModifiers()),
                method.getDeclaringClass()
        );
    }

    public static LParameter ofParameter(Parameter parameter) {
        if (parameter == null) return null;

        return new LParameter(
                parameter,
                parameter.getName(),
                parameter.getType()
        );
    }

    public static LModifier[] toModifiers(int modifiers) {
        return Arrays.stream(LModifier.values()).filter(m -> (modifiers & m.getMask()) != 0).toArray(LModifier[]::new);
    }

}
