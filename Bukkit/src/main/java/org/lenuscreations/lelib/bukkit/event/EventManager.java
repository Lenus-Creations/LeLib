package org.lenuscreations.lelib.bukkit.event;

import org.bukkit.event.Listener;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventManager {

    public void register(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(EventListener.class)) {
            for (Method method : clazz.getDeclaredMethods()) {
                this.register(method);
            }
            return;
        }

        if (clazz.getDeclaredMethods().length == 0) return;

        Method method = clazz.getDeclaredMethods()[0];
        this.register(method, clazz.getAnnotation(EventListener.class));
    }

    public void register(Method method) {
        if (!method.isAnnotationPresent(EventListener.class)) return;

        this.register(method, method.getAnnotation(EventListener.class));
    }

    private void register(Method method, EventListener annotation) {
        AbstractPlugin.getInstance().getServer().getPluginManager().registerEvent(annotation.event(), new Listener() {}, annotation.priority(), (e, l) -> {
            try {
                method.invoke(this, e);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }, AbstractPlugin.getInstance(), annotation.ignoreCancelled());
    }

}
