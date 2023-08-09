package org.lenuscreations.lelib.file.value.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lenuscreations.lelib.file.value.ConfigValue;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StringValue implements ConfigValue<String> {

    private String value;

    @Override
    public ConfigValue<String> parse(Object obj) {
        if (obj.getClass().getName().equals("String")) return new StringValue((String) obj);

        return new StringValue(obj.toString());
    }
    
}
