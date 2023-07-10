package org.lenuscreations.lelib.bukkit;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.lenuscreations.lelib.database.IDatabase;

public abstract class AbstractPlugin extends JavaPlugin {

    @Nullable
    @Getter
    private IDatabase<?, ?> currentDatabase = null;

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

    @SneakyThrows
    public void setDatabase(Class<? extends IDatabase<?, ?>> instance) {
        this.currentDatabase = instance.getDeclaredConstructor().newInstance();
        getLogger().info("The database has been set to " + this.currentDatabase + ".");
    }

}
