package org.lenuscreations.lelib.bukkit.command;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.SimplePluginManager;
import org.lenuscreations.lelib.LeLib;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.command.parameters.*;
import org.lenuscreations.lelib.command.Command;
import org.lenuscreations.lelib.command.ParameterType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHandler {

    private static boolean initialised = false;

    @Getter
    private static Map<Class<?>, ParameterType<?, CommandSender>> parameterTypes = new HashMap<>();
    protected static List<CommandNode> nodes = new ArrayList<>();

    @Setter
    public static String NO_PERMISSION_MESSAGE;
    @Setter
    public static String CONSOLE_ONLY_MESSAGE;
    @Setter
    public static String PLAYER_ONLY_MESSAGE;


    private static CommandMap map;

    @SneakyThrows
    private static void refreshMap() {
        if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
            SimplePluginManager spm = (SimplePluginManager) Bukkit.getPluginManager();

            Field commandMap = spm.getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);

            map = (CommandMap) commandMap.get(spm);
        }
    }

    @SneakyThrows
    public static void register(Class<?> clazz) {
        Object instance = clazz.getDeclaredConstructor().newInstance();
        for (Method method : clazz.getDeclaredMethods()) {
            Command command = method.getDeclaredAnnotation(Command.class);
            if (command == null) continue;

            String name = command.name();
            if (!name.contains(" ")) {
                CommandNode node = new CommandNode(method, command, instance);
                registerNode(node);
            }

            for (Method child : clazz.getDeclaredMethods()) {
                Command childCommand = child.getDeclaredAnnotation(Command.class);
                if (childCommand == null) continue;

                String childName = childCommand.name();
                if (childName.contains(" ")) {
                    String parent = childName.substring(0, childName.indexOf(" "));
                    childName = childName.substring(childName.indexOf(" ") + 1);

                    CommandNode foundParent = nodes.stream().filter(node -> node.getName().equals(parent)).findFirst().orElse(null);
                    if (foundParent == null) continue;

                    foundParent.addChild(childName, new CommandNode(child, childCommand, instance));
                }
            }
        }
    }

    private static void registerNode(CommandNode node) {
        nodes.add(node);

        LeLibHelpTopic helpTopic = new LeLibHelpTopic(node);
        BukkitCommand bukkitCommand = new BukkitCommand(node);
        map.register(AbstractPlugin.getInstance().getDescription().getName().toLowerCase(), bukkitCommand);

    }

    public static void registerParameter(Class<?> clazz, ParameterType<?, CommandSender> parameterType) {
        parameterTypes.put(clazz, parameterType);
    }

    public static void init() {
        if (initialised) return;
        refreshMap();

        registerParameter(String.class, new StringParameter());
        registerParameter(Boolean.class, new BooleanParameter());
        registerParameter(boolean.class, new BooleanParameter());
        registerParameter(Integer.class, new IntegerParameter());
        registerParameter(int.class, new IntegerParameter());
        registerParameter(Player.class, new PlayerParameter());
        registerParameter(OfflinePlayer.class, new OfflinePlayerParameter());
        registerParameter(World.class, new WorldParameter());
        registerParameter(Float.class, new FloatParameter());
        registerParameter(float.class, new FloatParameter());
        registerParameter(Double.class, new DoubleParameter());
        registerParameter(double.class, new DoubleParameter());

        NO_PERMISSION_MESSAGE = "&cNo permission.";
        CONSOLE_ONLY_MESSAGE = "&cConsole only.";
        PLAYER_ONLY_MESSAGE = "&cPlayer only.";

        initialised = true;
    }

}
