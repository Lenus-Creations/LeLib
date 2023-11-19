package org.lenuscreations.lelib.bukkit.gui;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.lenuscreations.lelib.bukkit.gui.buttons.GlassButton;
import org.lenuscreations.lelib.bukkit.gui.buttons.NextPageButton;
import org.lenuscreations.lelib.bukkit.gui.buttons.PreviousPageButton;

import java.util.HashMap;
import java.util.Map;

public abstract class PaginatedGUI extends GUI {

    @Setter
    @Getter
    protected int page;

    abstract public String getPerPageTitle(Player player);

    abstract public Map<Integer, Button> getPageButtons(Player player);

    public PaginatedGUI(int page) {
        this.page = page;
    }

    public PaginatedGUI() {
        this(1);
    }

    public int getPageSize() {
        return 9;
    }

    public int getMaxItemPerPage() {
        return 9;
    }

    @Override
    public final String getTitle(Player player) {
        return getPerPageTitle(player) + " [" + page + "/" + getMaxPage(player) + "]";
    }

    @SneakyThrows
    @Override
    public final Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i = 0; i < 9; i++) {
            buttons.put(i, new GlassButton());
        }

        int toAdd = 9 + (page == 1 ? 0 : page * 9);
        boolean nextPage = false;
        for (int i = toAdd; i < getMaxItemPerPage() + toAdd; i++) {
            Button button = getPageButtons(player).get(i);
            if (button == null) break;

            if (size() - 10 + toAdd < i) {
                nextPage = true;
                break;
            }

            buttons.put(i, button);
        }

        if (nextPage) {

            buttons.put(size() - 1, new NextPageButton(this));
        }

        if (page > 1) {
            buttons.put(size() - 9, new PreviousPageButton(this));
        }

        return buttons;
    }

    @Override
    public final int size() {
        return (9 * 2) + getPageSize();
    }

    private int getMaxPage(Player player) {
        return (getMaxItemPerPage() / getPageButtons(player).size()) + 1;
    }
}
