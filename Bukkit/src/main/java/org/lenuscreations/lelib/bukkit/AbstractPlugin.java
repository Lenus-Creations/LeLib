package org.lenuscreations.lelib.bukkit;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.lenuscreations.lelib.bukkit.server.IServer;
import org.lenuscreations.lelib.bukkit.utils.ClassUtils;
import org.lenuscreations.lelib.database.IDatabase;

public abstract class AbstractPlugin extends JavaPlugin {

    @Nullable
    @Getter
    private IDatabase<?, ?> currentDatabase = null;

    public IServer server;

    @Override
    public void onEnable() {
        super.onEnable();

        server = new IServer() {

            @Override
            public int getPlayerCount() {
                return getPlayerCount(false);
            }

            @Override
            public int getPlayerCount(boolean countInvisiblePlayers) {
                if (countInvisiblePlayers) {
                    return Bukkit.getServer().getOnlinePlayers().size();
                }

                int size = Bukkit.getServer().getOnlinePlayers().size();
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    for (Player player2 : Bukkit.getServer().getOnlinePlayers()) {
                        if (!player.canSee(player2)) {
                            size--;
                        }
                    }
                }

                return size;
            }

            @Override
            public int getMaxPlayerCount() {
                return Bukkit.getMaxPlayers();
            }

            @Override
            public void broadcast(String message) {
                Bukkit.getServer().broadcastMessage(message);
            }

            @Override
            public void broadcast(String message, String permission) {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (player.hasPermission(permission)) {
                        player.sendMessage(message);
                    }
                }
            }

            @Override
            public void setTabHeader(String header) {

            }

            @Override
            public void setTabFooter(String footer) {

            }

            @Override
            public void log(String message) {

            }

            @Override
            public void send(CommandSender sender, String message) {

            }

            @Override
            public ConsoleCommandSender getConsole() {
                return null;
            }
        };
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
