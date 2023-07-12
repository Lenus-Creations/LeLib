package org.lenuscreations.lelib.bukkit.gui;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.event.EventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GUIListener {

    @EventListener(event = InventoryClickEvent.class)
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Class<?> clazz = GUIHandler.openedGUIs.get(player);

        if (clazz == null) return;

        GUI gui = clazz.getDeclaredAnnotation(GUI.class);
        List<Method> listeners = Lists.newArrayList(clazz.getDeclaredMethods()).stream()
                .filter(method -> method.isAnnotationPresent(ClickListener.class))
                .collect(Collectors.toList());

        listeners.forEach(listener -> {
            ClickListener clickListener = listener.getAnnotation(ClickListener.class);

            int clickedSlot = e.getSlot();
            int[] slots = clickListener.slots();
            if (slots.length == 0) {
                // listen for item
                ItemStack clickedItem = e.getCurrentItem();
                if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(clickListener.itemName())) {
                    try {
                        listener.invoke(this, player, e.getClick(), clickedSlot);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                return;
            }

            // listen for slot
            if (Arrays.asList(slots).contains(clickedSlot)) {
                try {
                    listener.invoke(this, player, e.getClick(), clickedSlot);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @EventListener(event = InventoryCloseEvent.class)
    public void onClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;

        Player player = (Player) e.getPlayer();
        GUIHandler.openedGUIs.remove(player);
    }

}
