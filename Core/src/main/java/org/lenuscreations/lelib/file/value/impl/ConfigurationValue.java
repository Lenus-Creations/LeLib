package org.lenuscreations.lelib.file.value.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lenuscreations.lelib.file.Configuration;
import org.lenuscreations.lelib.file.value.ConfigValue;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationValue implements ConfigValue<Configuration> {

    private Configuration value;

    @Override
    public ConfigValue<Configuration> parse(Object obj) {
        return null;
    }
}
