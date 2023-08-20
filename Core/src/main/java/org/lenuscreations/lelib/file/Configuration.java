package org.lenuscreations.lelib.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.lenuscreations.lelib.file.value.ConfigValue;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Configuration {

    private final String name;

    private ConfigValue<?> value;

}
