package org.lenuscreations.lelib.bukkit;

import dev.grcq.v1_12_r1.V1_12_R1;
import dev.grcq.v1_16_r3.V1_16_R3;
import dev.grcq.v1_8_r3.V1_8_R3;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.lenuscreations.lelib.bukkit.annotations.Inject;
import org.lenuscreations.lelib.bukkit.annotations.ScheduledTask;
import org.lenuscreations.lelib.bukkit.chat.ChatInputListener;
import org.lenuscreations.lelib.bukkit.command.CommandHandler;
import org.lenuscreations.lelib.bukkit.config.ConfigHandler;
import org.lenuscreations.lelib.bukkit.disguise.DisguiseHandler;
import org.lenuscreations.lelib.bukkit.event.EventManager;
import org.lenuscreations.lelib.bukkit.gui.GUIListener;
import org.lenuscreations.lelib.bukkit.nick.NicknameHandler;
import org.lenuscreations.lelib.bukkit.npc.NPCHandler;
import org.lenuscreations.lelib.bukkit.server.IServer;
import org.lenuscreations.lelib.bukkit.tag.TagHandler;
import org.lenuscreations.lelib.bukkit.utils.Util;
import org.lenuscreations.lelib.database.old.IDatabase;
import org.lenuscreations.lelib.utils.ClassUtil;
import org.lenuscreations.lelib.utils.TimeUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Level;

public class Initialiser {

    private static final List<Integer> versionIgnores = Arrays.asList(17, 18, 19, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120);

    private DisguiseHandler disguiseHandler;
    private TagHandler tagHandler;
    private NicknameHandler nicknameHandler;
    private NPCHandler npcHandler;

    public IServer server;

    private Object object;

    public void onEnable(JavaPlugin plugin, Class<?> _clazz) {
        plugin.onEnable();

        this.disguiseHandler = new DisguiseHandler();
        this.tagHandler = new TagHandler();
        this.nicknameHandler = new NicknameHandler();
        this.npcHandler = new NPCHandler();
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
                        if (Util.getServerVersion() > 1122 || versionIgnores.contains(Util.getServerVersion())) {
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
                        if (Util.getServerVersion() > 1122 || versionIgnores.contains(Util.getServerVersion())) {
                            V1_16_R3.getTabHandler().setFooter(footer);
                        } else throw new UnsupportedOperationException("Not yet implemented");
                        break;
                }
            }

            @Override
            @SneakyThrows
            public void log(String message) {
                Bukkit.getServer().getLogger().log(Level.INFO, message);

                File file = new File("lelib_logs/log-" + new Date() + ".txt");
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }

                String log = "[" + TimeUtil.format(new Date()) + "] " + message + "\n";
                String previous;

                try (DataInputStream dis = new DataInputStream(Files.newInputStream(file.toPath()))) {
                    previous = dis.readUTF();
                }

                previous += log;

                DataOutputStream dos = new DataOutputStream(Files.newOutputStream(file.toPath()));
                dos.writeUTF(previous);
                dos.close();
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

        plugin.getServer().getPluginManager().registerEvents(new GUIListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ChatInputListener(), plugin);

        CommandHandler.init();
        //registerCommand(TestCommands.class);

        ConfigHandler.init(plugin);
        initScheduler(plugin, _clazz);

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                switch (Util.getNMSVersion()) {
                    case "v1_8_R3":
                        V1_8_R3.getTabHandler().send(p);
                        break;
                    case "v1_12_R1":
                        V1_12_R1.getTabHandler().send(p);
                        break;
                    default:
                        if (Util.getServerVersion() > 1122 || versionIgnores.contains(Util.getServerVersion())) {
                            V1_16_R3.getTabHandler().send(p);
                        } else throw new UnsupportedOperationException("Not yet implemented");
                        break;
                }
            }
        }, 10L, 10L);
    }

    private void initScheduler(JavaPlugin plugin, Class<?> _clazz) {
        for (Class<?> clazz : ClassUtil.getClassesInPackage(_clazz, _clazz.getPackage().getName())) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(ScheduledTask.class)) continue;

                ScheduledTask task = method.getAnnotation(ScheduledTask.class);
                if (task.async()) {
                    plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
                        try {
                            method.invoke(clazz.getDeclaredConstructor().newInstance());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, task.delay(), task.interval());
                } else {
                    plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                        try {
                            method.invoke(clazz.getDeclaredConstructor().newInstance());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, task.delay(), task.interval());
                }
            }
        }
    }

    @SneakyThrows
    public void initialise(JavaPlugin plugin, Class<?> mainClass) {
        this.onEnable(plugin, mainClass);

        object = mainClass.getDeclaredConstructor().newInstance();
        Field[] fields = mainClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                switch (field.getType().getSimpleName()) {
                    case "IServer":
                        field.set(object, this.server);
                        break;
                    case "DisguiseHandler":
                        field.set(object, this.disguiseHandler);
                        break;
                    case "TagHandler":
                        field.set(object, this.tagHandler);
                        break;
                    case "NicknameHandler":
                        field.set(object, this.nicknameHandler);
                        break;
                    case "NPCHandler":
                        field.set(object, this.npcHandler);
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported type for injection: " + field.getType().getSimpleName());
                }
            }
        }

        Method[] methods = mainClass.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Inject.class)) continue;

            Inject inject = method.getAnnotation(Inject.class);
            if (inject.whenDisabled()) continue;

            inject(plugin, method);
        }
    }

    @SneakyThrows
    public void disable(JavaPlugin plugin, Class<?> _clazz) {
        plugin.onDisable();

        Method[] methods = _clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Inject.class)) continue;

            Inject inject = method.getAnnotation(Inject.class);
            if (!inject.whenDisabled()) continue;

            inject(plugin, method);
        }
    }

    private void inject(JavaPlugin plugin, Method method) throws IllegalAccessException, InvocationTargetException {
        method.setAccessible(true);
        List<Object> params = new ArrayList<>();
        for (Class<?> param : method.getParameterTypes()) {
            switch (param.getSimpleName()) {
                case "IServer":
                    params.add(this.server);
                    break;
                case "DisguiseHandler":
                    params.add(this.disguiseHandler);
                    break;
                case "TagHandler":
                    params.add(this.tagHandler);
                    break;
                case "NicknameHandler":
                    params.add(this.nicknameHandler);
                    break;
                case "NPCHandler":
                    params.add(this.npcHandler);
                    break;
                case "JavaPlugin":
                    params.add(plugin);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported type for injection: " + param.getSimpleName());
            }
        }

        method.invoke(object, params.toArray());
    }

}
