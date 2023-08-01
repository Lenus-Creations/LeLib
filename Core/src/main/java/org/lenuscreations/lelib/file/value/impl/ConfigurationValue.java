package org.lenuscreations.lelib.file.value.impl;

import lombok.AllArgsConstructor;
import org.lenuscreations.lelib.file.Configuration;
import org.lenuscreations.lelib.file.value.ConfigValue;

@AllArgsConstructor
public class ConfigurationValue implements ConfigValue<Configuration> {

    private final Configuration configuration;

    @Override
    public Configuration getValue() {
        return configuration;
    }

    @Override
    public Configuration parse(Object obj) {
        return null;
    }
}
