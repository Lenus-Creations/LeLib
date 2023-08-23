package org.lenuscreations.lelib.bukkit;

import dev.grcq.v1_12_r1.V1_12_R1;
import dev.grcq.v1_16_r3.V1_16_R3;
import dev.grcq.v1_8_r3.V1_8_R3;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.lenuscreations.lelib.bukkit.command.CommandHandler;
import org.lenuscreations.lelib.bukkit.command.test.TestCommands;
import org.lenuscreations.lelib.bukkit.disguise.DisguiseHandler;
import org.lenuscreations.lelib.bukkit.event.EventManager;
import org.lenuscreations.lelib.bukkit.gui.GUIHandler;
import org.lenuscreations.lelib.bukkit.gui.GUIListener;
import org.lenuscreations.lelib.bukkit.hologram.HologramHandler;
import org.lenuscreations.lelib.bukkit.nick.NicknameHandler;
import org.lenuscreations.lelib.bukkit.server.IServer;
import org.lenuscreations.lelib.bukkit.tag.TagHandler;
import org.lenuscreations.lelib.bukkit.utils.ClassUtils;
import org.lenuscreations.lelib.bukkit.utils.Util;
import org.lenuscreations.lelib.database.Credentials;
import org.lenuscreations.lelib.database.IDatabase;
import org.lenuscreations.lelib.utils.ClassUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;

public class AbstractPlugin extends JavaPlugin {

    @Getter
    private static AbstractPlugin instance;

    @Nullable
    @Getter
    private IDatabase<?, ?> currentDatabase = null;

    @Getter
    private EventManager eventHandler;
    @Getter
    private GUIHandler guiHandler;
    @Getter
    private DisguiseHandler disguiseHandler;
    @Getter
    private TagHandler tagHandler;
    @Getter
    private NicknameHandler nicknameHandler;

    public IServer server;

    @Override
    public void onEnable() {
        super.onEnable();

        instance = this;

        this.eventHandler = new EventManager();
        this.guiHandler = new GUIHandler();
        this.disguiseHandler = new DisguiseHandler();
        this.tagHandler = new TagHandler();
        this.nicknameHandler = new NicknameHandler();
        this.server = new IServer() {

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
                switch (Util.getNMSVersion()) {
                    case "v1_8_R3":
                        V1_8_R3.getTabHandler().setHeader(header);
                        break;
                    case "v1_12_R1":
                        V1_12_R1.getTabHandler().setHeader(header);
                        break;
                    default:
                        if (Util.getServerVersion() > 1222) {
                            V1_16_R3.getTabHandler().setHeader(header);
                        } else throw new UnsupportedOperationException("Not yet implemented");
                        break;
                }
            }

            @Override
            public void setTabFooter(String footer) {
                switch (Util.getNMSVersion()) {
                    case "v1_8_R3":
                        V1_8_R3.getTabHandler().setFooter(footer);
                        break;
                    case "v1_12_R1":
                        V1_12_R1.getTabHandler().setFooter(footer);
                        break;
                    default:
                        if (Util.getServerVersion() > 1222) {
                            V1_16_R3.getTabHandler().setFooter(footer);
                        } else throw new UnsupportedOperationException("Not yet implemented");
                        break;
                }
            }

            @Override
            public void log(String message) {
                Bukkit.getServer().getLogger().log(Level.INFO, message);
                // TODO: write to file.
            }

            @Override
            public void send(CommandSender sender, String message) {
                sender.sendMessage(Util.format(message));
            }

            @Override
            public ConsoleCommandSender getConsole() {
                return Bukkit.getServer().getConsoleSender();
            }

            @Override
            public void eval(String command) {
                Bukkit.getServer().dispatchCommand(getConsole(), command);
            }
        };

        this.addListener(GUIListener.class);

        CommandHandler.init();
        //registerCommand(TestCommands.class);

        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                switch (Util.getNMSVersion()) {
                    case "v1_8_R3":
                        V1_8_R3.getTabHandler().send(p);
                        break;
                    case "v1_12_R1":
                        V1_12_R1.getTabHandler().send(p);
                        break;
                    default:
                        if (Util.getServerVersion() > 1222) {
                            V1_16_R3.getTabHandler().send(p);
                        } else throw new UnsupportedOperationException("Not yet implemented");
                        break;
                }
            }
        }, 10L, 10L);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public final void registerCommand(Class<?> clazz) {
        CommandHandler.register(clazz);
    }

    public final void registerCommand(Class<?>... clazz) {
        Arrays.asList(clazz).forEach(this::registerCommand);
    }

    public final void registerCommands(AbstractPlugin plugin) {
        ClassUtil.getClassesInPackage(plugin.getClass(), plugin.getClass().getPackage().getName()).forEach(this::registerCommand);
    }

    public final void addListener(Class<?> clazz) {
        this.eventHandler.register(clazz);
    }

    public final void addListener(Class<?>... clazz) {
        Arrays.asList(clazz).forEach(this::addListener);
    }

    @SneakyThrows
    public final void setDatabase(Class<? extends IDatabase<?, ?>> instance, Credentials credentials) {
        this.currentDatabase = instance.getDeclaredConstructor().newInstance();
        this.currentDatabase.setCredentials(credentials);
        getLogger().info("The database has been set to " + this.currentDatabase + ".");
    }

}
