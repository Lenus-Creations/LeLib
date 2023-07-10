package org.lenuscreations.lelib.file;

import lombok.Builder;
import lombok.Data;
import org.lenuscreations.lelib.file.value.ConfigValue;

import java.util.List;

@Data
@Builder
public class Configuration {

    private final String name;

    private final List<Configuration> children;

    private ConfigValue<?> value;

}
