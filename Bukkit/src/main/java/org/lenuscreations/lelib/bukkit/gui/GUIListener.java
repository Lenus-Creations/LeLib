package org.lenuscreations.lelib.bukkit.gui;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.event.EventListener;
import org.lenuscreations.lelib.bukkit.utils.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GUIListener {

    @EventListener(event = InventoryClickEvent.class)
    public static void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Class<?> clazz = GUIHandler.openedGUIs.get(player);

        if (clazz == null) return;

        List<Method> listeners = Lists.newArrayList(clazz.getDeclaredMethods()).stream()
                .filter(method -> method.isAnnotationPresent(ClickListener.class))
                .collect(Collectors.toList());

        listeners.forEach(listener -> {
            ClickListener clickListener = listener.getAnnotation(ClickListener.class);
            AbstractPlugin.getInstance().server.log(clickListener.itemName());

            if (clickListener.clickTypes().length != 0 && !Arrays.asList(clickListener.clickTypes()).contains(e.getClick())) {
                e.setCancelled(true);
                return;
            }

            int clickedSlot = e.getSlot();
            int[] slots = clickListener.slots();
            List<Integer> slotList = Arrays.stream(slots).boxed().collect(Collectors.toList());

            if (slots.length == 0) {
                ItemStack clickedItem = e.getCurrentItem();
                if (clickedItem.getItemMeta() == null) {
                    e.setCancelled(true);
                    return;
                }

                if (Util.format(clickedItem.getItemMeta().getDisplayName()).equalsIgnoreCase(Util.format(clickListener.itemName()))) {
                    try {
                        boolean cancelClick = (boolean) listener.invoke(clazz.getDeclaredConstructor().newInstance(), player, e.getClick(), clickedSlot);
                        e.setCancelled(cancelClick);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                             InstantiationException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                return;
            }

            // listen for slot
            if (slotList.contains(clickedSlot)) {
                try {
                    boolean cancelClick = (boolean) listener.invoke(clazz.getDeclaredConstructor().newInstance(), player, e.getClick(), clickedSlot);
                    e.setCancelled(cancelClick);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                         InstantiationException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @EventListener(event = InventoryCloseEvent.class)
    public static void onClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        Player player = (Player) e.getPlayer();

        if (!GUIHandler.tasks.containsKey(player)) return;

        try {
            GUIHandler.tasks.get(player).cancel();
        } catch (Exception ignored) {}

        GUIHandler.openedGUIs.remove(player);
        GUIHandler.tasks.remove(player);
    }

}
