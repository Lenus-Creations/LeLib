package org.lenuscreations.velocity.command.arguments;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Argument {

    private final ArgumentType type;
    private final String name;
    private final Object value;

    private String flagName;
}
