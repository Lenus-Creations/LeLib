package org.lenuscreations.lelib.file.value.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lenuscreations.lelib.file.value.ConfigValue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoubleValue implements ConfigValue<Double> {

    private Double value;

    @Override
    public ConfigValue<Double> parse(Object obj) {
        return (obj.getClass().getSimpleName().equalsIgnoreCase("double")) ? new DoubleValue((Double) obj) : null;
    }
}
