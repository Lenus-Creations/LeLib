package org.lenuscreations.lelib.file;

import com.google.common.reflect.TypeToken;
import org.bson.json.JsonReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lenuscreations.lelib.LeLib;
import org.lenuscreations.lelib.file.value.ConfigValue;
import org.lenuscreations.lelib.file.value.impl.ConfigurationValue;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.*;

public class FileHandler {

    private final String path;
    private final List<Configuration> config;

    private final Map<Class<?>, ConfigValue<?>> registeredValues;

    public FileHandler(File file) {
        this(file.getPath());
    }

    public FileHandler(String path) {
        this.path = path;
        this.config = new ArrayList<>();
        this.registeredValues = new HashMap<>();

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
                            .value(this.parseValue(key, value));

        });
    }

    private ConfigValue<?> parseValue(String key, Object value) {
        if (value.getClass().getSuperclass() == HashMap.class) {
            Map<String, Object> parsed = (HashMap<String, Object>) value;
            
            //child = new Configuration(key, this.parseValue(parsed));
            //return new ConfigurationValue(child);
        }

        return null;
    }

    public void set(Configuration configuration) {
        config.removeIf(c -> c.getName().equalsIgnoreCase(configuration.getName()));

        config.add(configuration);
    }

    public void set(Configuration.ConfigurationBuilder builder) {
        this.set(builder.build());
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public Configuration getConfig(@NotNull String path) {
        String[] fullPath = path.split("\\.");
        if (fullPath.length == 0) throw new RuntimeException("Invalid path.");

        Optional<Configuration> parent = config.stream().filter(c -> c.getName().equalsIgnoreCase(fullPath[0])).findFirst();
        for (int i = 1; i < fullPath.length; i++) {
            String name = fullPath[i];
            if (!parent.isPresent()) throw new RuntimeException("Path does not exist.");

            Configuration cfg = parent.get();

        }

        return parent.orElse(null);
    }

    public void save() {
        // todo: write file
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

    public void registerValue(Class<?> clazz, ConfigValue<?> configValue) {
        registeredValues.put(clazz, configValue);
    }
}
