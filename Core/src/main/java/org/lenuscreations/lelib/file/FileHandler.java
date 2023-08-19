package org.lenuscreations.lelib.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lenuscreations.lelib.LeLib;
import org.lenuscreations.lelib.file.value.ConfigValue;
import org.lenuscreations.lelib.file.value.impl.*;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class FileHandler {

    private final String path;
    private final List<Configuration> config;

    @Getter
    private static final Map<Class<?>, ConfigValue<?>> registeredValues = new HashMap<>();

    public FileHandler(File file, Class<?> main) {
        this(file.getPath(), main);
    }

    public FileHandler(String path, Class<?> main) {
        this.path = path;
        this.config = new ArrayList<>();

        this.parse(main);
    }

    private void parse(Class<?> main) {
        Type mapType = new TypeToken<HashMap<String, Object>>() {}.getType();

        Map<String, Object> obj;
        switch (getFileType()) {
            case YAML:
                Yaml yaml = new Yaml();
                try (InputStream is = main.getClassLoader().getResourceAsStream(path)) {
                    obj = yaml.load(is);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case JSON:
                try {
                    URL url = main.getClassLoader().getResource(path);
                    obj = LeLib.GSON.fromJson(new FileReader(url.getPath()), mapType);
                    String json = LeLib.GSON.toJson(obj);

                    ObjectMapper mapper = new ObjectMapper();
                    obj = mapper.readValue(json, Map.class);
                } catch (FileNotFoundException | JsonProcessingException e) {
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
        if (value == null) return null;

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

    public void save(Class<?> main) {
        // TODO: Fix save method (implement Configuration.class adapters for SnakeYaml and Gson)
        JsonObject obj = unparse();
        Map<String, Object> map = LeLib.GSON.fromJson(obj, new TypeToken<Map<String, Object>>() {}.getType());
        URL url = main.getClassLoader().getResource(path);

        switch (getFileType()) {
            case JSON:
                try (FileWriter writer = new FileWriter(url.getFile())) {
                    LeLib.GSON_PPG.toJson(map, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case YAML:
                Yaml yaml = new Yaml();
                try (FileWriter writer = new FileWriter(url.getFile())) {
                    yaml.dump(map, writer);
                    System.out.println(map);
                    System.out.println(url.getFile());
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

    private JsonObject unparse() {
        JsonObject object = new JsonObject();
        for (Configuration configuration : config) {
            object.add(configuration.getName(), unparse(configuration));
        }

        return object;
    }

    private JsonElement unparse(Configuration configuration) {
        JsonElement element = null;

        if (configuration.getValue() == null) return null;

        if (configuration.getValue().getClass() == ConfigurationChildren.class) {
            element = new JsonObject();
            for (Configuration child : ((ConfigurationChildren) configuration.getValue()).getValue()) {
                ((JsonObject) element).add(child.getName(), unparse(child));
            }
        } else if (configuration.getValue().getClass() == StringValue.class) {
            element = new JsonPrimitive(((StringValue) configuration.getValue()).getValue());
        } else if (configuration.getValue().getClass() == DoubleValue.class) {
            element = new JsonPrimitive(((DoubleValue) configuration.getValue()).getValue());
        } else if (configuration.getValue().getClass() == FloatValue.class) {
            element = new JsonPrimitive(((FloatValue) configuration.getValue()).getValue());
        } else if (configuration.getValue().getClass() == BoolValue.class) {
            element = new JsonPrimitive(((BoolValue) configuration.getValue()).getValue());
        }

        return element;
    }

    public static void registerValue(Class<?> clazz, ConfigValue<?> configValue) {
        registeredValues.put(clazz, configValue);
    }

    static {
        registerValue(Boolean.class, new BoolValue());
        registerValue(String.class, new StringValue());
        registerValue(Configuration.class, new ConfigurationValue());
        registerValue(Double.class, new DoubleValue());
        registerValue(Float.class, new FloatValue());
        registerValue(Integer.class, new IntValue());
    }
}
