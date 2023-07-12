package org.lenuscreations.lelib.bukkit.gui;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.lenuscreations.lelib.bukkit.utils.ItemBuilder;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GUIHandler {

    public static Map<Player, Class<?>> openedGUIs = new HashMap<>();

    private final Map<Class<?>, Method> handlers;

    public GUIHandler() {
        this.handlers = new HashMap<>();
    }

    @SneakyThrows
    public void openGUI(Player player, Class<?> clazz) {
        if (!clazz.isAnnotationPresent(GUI.class)) return;

        if (clazz.getDeclaredMethods().length == 0) return;

        GUI gui = clazz.getDeclaredAnnotation(GUI.class);
        Method method = clazz.getDeclaredMethods()[0];

        if (method.getReturnType() != Map.class) return;
        Type[] types = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments();

        if (types.length != 2) return;

        Class<?> type1 = (Class<?>) types[0];
        Class<?> type2 = (Class<?>) types[1];

        if (type1.getDeclaringClass() != Integer.class || type2.getDeclaringClass() != ItemStack.class) return;

        Inventory inventory;
        if (gui.type() == InventoryType.CHEST) {
            inventory = Bukkit.getServer().createInventory(null, gui.size(), gui.title());
        } else {
            inventory = Bukkit.createInventory(null, gui.type(), gui.title());
        }

        @SuppressWarnings("unchecked")
        Map<Integer, ItemStack> items = (Map<Integer, ItemStack>) method.invoke(this, player);

        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue());
        }
    }

    public void registerGUI(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(GUI.class)) return;
        if (clazz.getDeclaredMethods().length == 0) return;

        Method method = clazz.getDeclaredMethods()[0];
        if (method.getReturnType() != Map.class) return;
        Type[] types = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments();

        if (types.length != 2) return;

        Class<?> type1 = (Class<?>) types[0];
        Class<?> type2 = (Class<?>) types[1];

        if (type1.getDeclaringClass() != Integer.class || type2.getDeclaringClass() != ItemStack[].class) return;

        handlers.put(clazz, clazz.getDeclaredMethods()[0]);
    }

}