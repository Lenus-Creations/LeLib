package org.lenuscreations.lelib.file.value.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lenuscreations.lelib.file.value.ConfigValue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoolValue implements ConfigValue<Boolean> {

    private Boolean value;

    @Override
    public ConfigValue<Boolean> parse(Object obj) {
        return (obj.getClass().getSimpleName().equalsIgnoreCase("boolean")) ? new BoolValue((Boolean) obj) : null;
    }
}