package org.lenuscreations.lelib.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileHandler {

    private final File file;
    private final List<Configuration> config;

    public FileHandler(File file) {
        this.file = file;
        this.config = new ArrayList<>();

        this.parse();
    }

    private void parse() {
        // todo: read file and add to this::config
        switch (getFileType()) {
            case YAML:
                break;
            case JSON:
                break;
        }
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
            parent = cfg.getChildren().stream().filter(c -> c.getName().equalsIgnoreCase(name)).findFirst();
        }

        return parent.orElse(null);
    }

    public void save() {
        // todo: write file
    }

    public FileType getFileType() {
        String[] a = file.getName().split("\\.");
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
}
