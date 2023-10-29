package org.lenuscreations.velocity.command.parameters;

import com.velocitypowered.api.command.CommandSource;
import org.lenuscreations.lelib.command.ParameterType;

public class StringParameter implements ParameterType<String, CommandSource> {

    @Override
    public String parse(CommandSource executor, String target) {
        return target;
    }

}
