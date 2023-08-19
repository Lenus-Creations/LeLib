package org.lenuscreations.lelib.file.value.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lenuscreations.lelib.file.value.ConfigValue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FloatValue implements ConfigValue<Float> {

    private Float value;

    @Override
    public ConfigValue<Float> parse(Object obj) {
        return (obj.getClass().getSimpleName().equalsIgnoreCase("float")) ? new FloatValue((float) obj) : null;
    }
}
