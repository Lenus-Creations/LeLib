package org.lenuscreations.lelib.bukkit.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.lenuscreations.lelib.bukkit.event.EventListener;

public class GUIListener {

    @EventListener(priority = EventPriority.HIGHEST, event = InventoryCloseEvent.class)
    public static void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        GUI gui = GUI.getGuiMap().get(player.getUniqueId());
        if (gui != null) gui.onClose(player);

        GUI.getGuiMap().remove(player.getUniqueId());
        BukkitRunnable runnable = GUI.getRunnableMap().get(player.getUniqueId());
        if (runnable != null) runnable.cancel();

        GUI.getRunnableMap().remove(player.getUniqueId());
    }

    @EventListener(priority = EventPriority.HIGHEST, event = InventoryClickEvent.class)
    public static void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GUI gui = GUI.getGuiMap().get(player.getUniqueId());
        if (gui == null) {
            return;
        }

        MenuItem item = gui.getContent(player, event.getInventory()).get(event.getSlot());
        if (item == null) {
            return;
        }

        if (item.cancelClick()) {
            event.setCancelled(true);
        }

        item.onClick(player, event.getClick(), event.getSlot());
    }

}
