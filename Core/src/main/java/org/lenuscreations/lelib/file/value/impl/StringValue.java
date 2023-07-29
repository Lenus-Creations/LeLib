package org.lenuscreations.lelib.file.value.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.lenuscreations.lelib.file.value.ConfigValue;

@Data
@AllArgsConstructor
public class StringValue implements ConfigValue<String> {

    private String value;

    @Override
    public String parse(Object obj) {
        if (obj.getClass() == String.class) return (String) obj;

        return obj.toString();
    }
    
}
