package org.lenuscreations.lelib.bukkit.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.annotations.ScheduledTask;
import org.lenuscreations.lelib.utils.ClassUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ConfigHandler {

    private static final List<Runnable> configs = new ArrayList<>();

    public static void init(JavaPlugin plugin) {
        Collection<Class<?>> classes = ClassUtil.getClassesInPackage(plugin.getClass(), plugin.getClass().getPackage().getName());
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Configuration.class)) {
                Configuration configuration = clazz.getAnnotation(Configuration.class);

                Runnable runnable = () -> {
                    String fileName = configuration.value();
                    String extension = configuration.type().getExtensions()[0];

                    File file = new File(plugin.getDataFolder(), fileName + "." + extension);
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    if (configuration.type() == Configuration.Type.YAML) {
                        YamlConfiguration yamlConfiguration = new YamlConfiguration();
                        try {
                            yamlConfiguration.load(file);
                        } catch (IOException | InvalidConfigurationException e) {
                            throw new RuntimeException(e);
                        }

                        for (Field field : clazz.getDeclaredFields()) {
                            if (field.isAnnotationPresent(ConfigValue.class)) {
                                field.setAccessible(true);

                                ConfigValue configValue = field.getAnnotation(ConfigValue.class);
                                String path = configValue.value();
                                Object value = yamlConfiguration.get(path);

                                try {
                                    if (value == null) {
                                        value = field.get(null);
                                        yamlConfiguration.set(path, value);
                                    }
                                    
                                    field.set(null, value);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                        try {
                            yamlConfiguration.save(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                };

                plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, 0, 20L);
            }
        }
    }

    @ScheduledTask(interval = 20L, async = true)
    public void loadFromConfig() {
        AbstractPlugin plugin = AbstractPlugin.getInstance();
        Collection<Class<?>> classes = ClassUtil.getClassesInPackage(plugin.getClass(), plugin.getClass().getPackage().getName());
        for (Class<?> clazz : classes) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.isAnnotationPresent(FromConfig.class)) {
                    continue;
                }

                FromConfig fromConfig = field.getAnnotation(FromConfig.class);
                String path = fromConfig.value();

                plugin.reloadConfig();

                org.bukkit.configuration.Configuration configuration = plugin.getConfig();
                Object value = configuration.get(path);

                try {
                    field.set(clazz.getDeclaredConstructor().newInstance(), value);
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException |
                         InstantiationException e) {
                    try {
                        field.set(null, value);
                    } catch (IllegalAccessException illegalAccessException) {
                        illegalAccessException.printStackTrace();
                    }
                }
            }
        }
    }

}
