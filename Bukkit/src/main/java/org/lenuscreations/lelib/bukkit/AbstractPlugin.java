package org.lenuscreations.lelib.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public final void registerCommand() {

    }
}
