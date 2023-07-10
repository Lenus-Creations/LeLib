package org.lenuscreations.letlib.file;

import lombok.Builder;
import lombok.Data;
import org.lenuscreations.letlib.file.value.ConfigValue;

import java.util.List;

@Data
@Builder
public class Configuration {

    private final String name;

    private final List<Configuration> children;

    private ConfigValue<?> value;

}
