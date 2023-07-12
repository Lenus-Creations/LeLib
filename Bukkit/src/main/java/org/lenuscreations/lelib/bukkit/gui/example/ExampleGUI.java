package org.lenuscreations.lelib.bukkit.gui.example;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.gui.ClickListener;
import org.lenuscreations.lelib.bukkit.gui.GUI;
import org.lenuscreations.lelib.bukkit.gui.GUIHandler;

import java.util.HashMap;
import java.util.Map;

@GUI(title = "Example GUI", size = 9 * 5)
public class ExampleGUI {

    public ExampleGUI(Player player) {
        GUIHandler handler = AbstractPlugin.getInstance().getGuiHandler();

        // Open the GUI using the GUIHandler class.
        handler.openGUI(player, ExampleGUI.class);
    }

    /*
     * You can name the methods whatever you'd like.
     * The handler will try to use the first method it finds.
     * Might make an annotation to declare which method will be used, like @Inventory.
     */
    public Map<Integer, ItemStack> inventory(Player player) {
        Map<Integer, ItemStack> items = new HashMap<>();

        /*
         * The integer is the slot.
         */
        items.put(1, new ItemStack(Material.IRON_SWORD));

        ItemStack item = new ItemStack(Material.WOOD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Test");

        item.setItemMeta(meta);

        items.put(2, item);

        return items;
    }

    /*
     * You can listen for which slot is clicked. Leave `clickTypes` empty if
     * you don't want to use a specific click type.
     * You can listen for multiple slots.
     */
    @ClickListener(slot = {1})
    public boolean onSlotClick(Player player, ClickType type, int slot) {
        // Return true if you want to cancel the click (not the whole event, just the item pick-up).
        return true;
    }

    /*
     * You can listen for which item is clicked.
     */
    @ClickListener(itemName = "Test", clickType = {ClickType.RIGHT, ClickType.SHIFT_RIGHT})
    public boolean onItemClick(Player player, ClickType type, int slot) {
        return true;
    }

}
