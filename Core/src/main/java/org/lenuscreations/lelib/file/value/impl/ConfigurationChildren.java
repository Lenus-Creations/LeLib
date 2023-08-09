package org.lenuscreations.lelib.file.value.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.lenuscreations.lelib.file.Configuration;
import org.lenuscreations.lelib.file.FileHandler;
import org.lenuscreations.lelib.file.value.ConfigValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class ConfigurationChildren implements ConfigValue<Collection<Configuration>> {

    private Collection<Configuration> children = new ArrayList<>();

    public ConfigurationChildren(Collection<Configuration> children) {
        this.children = children;
    }

    @Override
    public Collection<Configuration> getValue() {
        return children;
    }

    @Override
    public ConfigValue<Collection<Configuration>> parse(Object obj) {
        if (obj.getClass().getSuperclass() != HashMap.class) throw new RuntimeException("Invalid cast.");

        Map<String, Object> parsed = (HashMap<String, Object>) obj;
        parsed.forEach((key, value) -> {
            Configuration configuration =
                    Configuration.builder()
                        .name(key)
                        .value(FileHandler.parseValue(key, value))
                        .build();

            children.add(configuration);
        });

        return new ConfigurationChildren(children);
    }

    public void addChild(Configuration configuration) {
        children.add(configuration);
    }

    public void removeChild(Configuration configuration) {
        if (configuration == null) return;
        children.remove(configuration);
    }

    public void removeChild(String pathName) {
        this.removeChild(children.stream().filter(c -> c.getName().equals(pathName)).findFirst().orElse(null));
    }

}
