package org.lenuscreations.lelib.bukkit.gui.old;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class GUIHandler {

    public static Map<Player, Class<?>> openedGUIs = new HashMap<>();
    public static Map<Player, BukkitRunnable> tasks = new HashMap<>();

    private final Map<Class<?>, Method> handlers;

    public GUIHandler() {
        this.handlers = new HashMap<>();
    }

    @SneakyThrows
    public void openGUI(Player player, Class<?> clazz) {
        if (!clazz.isAnnotationPresent(GUI.class)) return;
        if (clazz.getDeclaredMethods().length == 0) return;

        GUI gui = clazz.getDeclaredAnnotation(GUI.class);
        Method method = Arrays.stream(clazz.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(GUIExecutor.class)).findFirst().orElse(null);
        if (method == null) return;

        if (method.getReturnType() != Map.class) return;
        Type[] types = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments();

        if (types.length != 2) return;

        Class<?> type1 = (Class<?>) types[0];
        Class<?> type2 = (Class<?>) types[1];

        if (type1 != Integer.class || type2 != ItemStack.class) return;

        Inventory inventory;
        if (gui.type() == InventoryType.CHEST) {
            inventory = Bukkit.getServer().createInventory(null, gui.size(), gui.title());
        } else {
            inventory = Bukkit.createInventory(null, gui.type(), gui.title());
        }

        player.openInventory(inventory);

        @SuppressWarnings("unchecked")
        Map<Integer, ItemStack> items = (Map<Integer, ItemStack>) method.invoke(clazz.getDeclaredConstructor().newInstance(), player);

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
                    inventory.setItem(entry.getKey(), entry.getValue());
                }
            }
        };

        runnable.run();
        tasks.put(player, runnable);
        openedGUIs.put(player, clazz);

        if (gui.autoUpdate()) {
            runnable.runTaskTimerAsynchronously(AbstractPlugin.getInstance(), gui.autoUpdateTime(), gui.autoUpdateTime());
        }
    }

}
