package org.lenuscreations.lelib.bukkit.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ClassUtils {

    @SneakyThrows
    public static Class<?> getNMSClass(String name) {
        return Class.forName("net.minecraft.server." + Util.getNMSVersion() + "." + name);
    }

    @SneakyThrows
    public static Class<?> getOBCClass(String name) {
        return Class.forName("org.bukkit.craftbukkit." + Util.getNMSVersion() + "." + name);
    }

}
