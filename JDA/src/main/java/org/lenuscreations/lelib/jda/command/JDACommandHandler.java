package org.lenuscreations.lelib.jda.command;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.lenuscreations.lelib.command.ParameterType;

import java.awt.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class JDACommandHandler {

    /* ----------------- Options ----------------- */

    public static String USAGE_MESSAGE = "Invalid usage!\nUsage: %s";
    public static EmbedBuilder USAGE_MESSAGE_EMBED = new EmbedBuilder()
            .setColor(Color.RED)
            .setDescription("Invalid usage!\nUsage: %s")
            .setTimestamp(Instant.now());
    public static Options USAGE_MESSAGE_OPTION = Options.EMBED;

    public static String COMMAND_MESSAGE_PREFIX = "!";

    /* ------------------------------------------- */

    @Getter
    private static Map<Class<?>, ParameterType<?, Member>> parameterTypes = new HashMap<>();

}
