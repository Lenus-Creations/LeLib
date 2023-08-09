package org.lenuscreations.lelib.file;

import com.google.common.reflect.TypeToken;
import lombok.Getter;
import org.bson.json.JsonReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lenuscreations.lelib.LeLib;
import org.lenuscreations.lelib.file.value.ConfigValue;
import org.lenuscreations.lelib.file.value.impl.ConfigurationChildren;
import org.lenuscreations.lelib.file.value.impl.ConfigurationValue;
import org.lenuscreations.lelib.file.value.impl.StringValue;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class FileHandler {

    private final String path;
    private final List<Configuration> config;

    @Getter
    private static final Map<Class<?>, ConfigValue<?>> registeredValues = new HashMap<>();

    public FileHandler(File file) {
        this(file.getPath());
    }

    public FileHandler(String path) {
        this.path = path;
        this.config = new ArrayList<>();

        this.parse();
    }

    private void parse() {
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();

        Map<String, Object> obj;
        switch (getFileType()) {
            case YAML:
                Yaml yaml = new Yaml();
                try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
                    obj = yaml.load(is);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case JSON:
                try {
                    obj = LeLib.GSON.fromJson(new FileReader(path), mapType);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                throw new RuntimeException("Invalid file type.");
        }

        obj.forEach((key, value) -> {
            Configuration.ConfigurationBuilder builder =
                    Configuration.builder()
                            .name(key)
                            .value(parseValue(key, value));
            config.add(builder.build());
        });
    }

    public static ConfigValue<?> parseValue(String key, Object value) {
        if (value.getClass().getSuperclass() == HashMap.class) {
            Map<String, Object> parsed = (HashMap<String, Object>) value;

            ConfigurationChildren children = new ConfigurationChildren();
            children.parse(parsed);

            return children;
        }

        ConfigValue<?> configValue = getRegisteredValues().get(value.getClass());
        if (configValue == null) throw new RuntimeException("Unregistered ConfigValue. Could not find value for " + value.getClass());

        configValue = configValue.parse(value);
        return configValue;
    }

    public void set(Configuration configuration) {
        config.removeIf(c -> c.getName().equalsIgnoreCase(configuration.getName()));

        config.add(configuration);
    }

    public void set(Configuration.ConfigurationBuilder builder) {
        this.set(builder.build());
    }

    @Nullable
    public Configuration getConfig(@NotNull String path) {
        String[] fullPath = path.split("\\.");
        if (fullPath.length == 0) throw new RuntimeException("Invalid path.");

        Optional<Configuration> parent = config.stream().filter(c -> c.getName().equalsIgnoreCase(fullPath[0])).findFirst();
        for (int i = 1; i < fullPath.length; i++) {
            String name = fullPath[i];
            if (!parent.isPresent()) throw new RuntimeException("Path does not exist.");

            Configuration cfg = parent.get();
            if (cfg.getValue() instanceof ConfigurationChildren) {
                ConfigurationChildren children = (ConfigurationChildren) cfg.getValue();

                parent = children.getValue().stream().filter(c -> c.getName().equals(name)).findFirst();
            }
        }

        return parent.orElse(null);
    }

    public void save() {
        // TODO: Fix save method (implement Configuration.class adapters for SnakeYaml and Gson)
        switch (getFileType()) {
            case JSON:
                try (FileWriter writer = new FileWriter(path)) {
                    LeLib.GSON_PPG.toJson(config, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case YAML:
                Yaml yaml = new Yaml();
                System.out.println(config);
                try (FileWriter writer = new FileWriter(path)) {
                    yaml.dump(config, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                // Cannot happen either way.
                throw new RuntimeException();
        }
    }

    public FileType getFileType() {
        String[] a = path.split("\\.");
        String type = a[a.length - 1].toLowerCase();
        switch (type) {
            case "json":
                return FileType.JSON;
            case "yml":
            case "yaml":
                return FileType.YAML;
            default:
                throw new UnsupportedOperationException("Unsupported file type; not yet implemented.");
        }
    }

    public static void registerValue(Class<?> clazz, ConfigValue<?> configValue) {
        registeredValues.put(clazz, configValue);
    }

    static {
        registerValue(String.class, new StringValue());
        registerValue(Configuration.class, new ConfigurationValue());
    }
}
