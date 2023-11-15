package org.lenuscreations.lelib.bukkit.gui;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.gui.defaults.GlassPaneItem;
import org.lenuscreations.lelib.bukkit.gui.defaults.NextPageItem;
import org.lenuscreations.lelib.bukkit.gui.defaults.PreviousPageItem;

import java.util.HashMap;
import java.util.Map;

public abstract class PaginatedGUI extends GUI {

    private int page;

    public PaginatedGUI(int page) {
        this.page = page;
    }

    public PaginatedGUI() {
        this(1);
    }

    public final void setPage(int page, Player player) {
        this.page = page;
        BukkitRunnable runnable = GUI.getRunnableMap().get(player.getUniqueId());
        runnable.run();
    }

    abstract public String getTitlePerPage(Player player, int page);

    abstract public Map<Integer, MenuItem> getPageContent(Player player);

    @Override
    public final int getSize(Player player) {
        return 9 + getMaxItemsPerPage();
    }

    @Override
    public String getTitle(Player player) {
        return getTitlePerPage(player, 1) + "[" + page + "/" + getMaxPage(player) + "]";
    }

    public int getMaxItemsPerPage() {
        return 9;
    }

    private int getMaxPage(Player player) {
        return (getMaxItemsPerPage() / getPageContent(player).size()) + 1;
    }

    @Override
    public final Map<Integer, MenuItem> getContent(Player player, Inventory inventory) {
        Map<Integer, MenuItem> buttons = new HashMap<>();

        for (int i = 0; i < 9; i++) {
            buttons.put(i, new GlassPaneItem());
        }

        int toAdd = 9 + (page == 1 ? 0 : page * 9);
        boolean nextPage = false;
        for (int i = toAdd; i < getMaxItemsPerPage() + toAdd; i++) {
            MenuItem button = getPageContent(player).get(i);
            if (button == null) break;

            if (getSize(player) - 10 + toAdd < i) {
                nextPage = true;
                break;
            }

            buttons.put(i, button);
        }

        if (nextPage) {
            buttons.put(getSize(player) - 1, new NextPageItem(this));
        }

        if (page > 1) {
            buttons.put(getSize(player) - 9, new PreviousPageItem(this));
        }

        return buttons;
    }

    public final void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, getSize(player) + 9, getTitle(player));

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                getContent(player, inventory).forEach((slot, item) -> inventory.setItem(slot, toItemStack(player, item)));
            }
        };
        runnable.run();

        GUISettings settings = this.getClass().getDeclaredAnnotation(GUISettings.class);
        if (settings != null) {
            if (settings.autoUpdate()) {
                runnable.runTaskTimerAsynchronously(AbstractPlugin.getInstance(), settings.autoUpdateTime(), settings.autoUpdateTime());
            }
        }

        GUI.getRunnableMap().put(player.getUniqueId(), runnable);
        player.openInventory(inventory);
    }

}
