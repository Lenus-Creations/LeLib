package org.lenuscreations.lelib.file.value.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lenuscreations.lelib.file.value.ConfigValue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntValue implements ConfigValue<Integer> {

    private Integer value;

    @Override
    public ConfigValue<Integer> parse(Object obj) {
        return (obj.getClass().getSimpleName().equalsIgnoreCase("integer")) ? new IntValue((Integer) obj) : null;
    }

}
