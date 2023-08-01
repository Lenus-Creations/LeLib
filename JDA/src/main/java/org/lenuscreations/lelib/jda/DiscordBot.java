package org.lenuscreations.lelib.jda;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.lenuscreations.lelib.jda.command.CommandNode;

import java.util.ArrayList;
import java.util.Collection;

public abstract class DiscordBot {

    @Getter
    private JDA jda;

    private final JDABuilder builder;

    public DiscordBot() {
        this.builder = JDABuilder
                .create(getToken(), getIntents());
    }

    abstract protected Collection<Class<?>> getListeners();

    abstract protected Collection<CommandNode> getCommands();

    abstract public char getPrefix();

    abstract public @NotNull String getToken();

    public @NotNull Collection<GatewayIntent> getIntents() {
        return new ArrayList<>();
    }

    public final JDA start() {
        if (this.jda != null) return this.jda;

        return this.jda = builder.build();
    }

    public final void stop() {
        this.jda.shutdown();
        this.jda = null;
    }

    public final void kill() {
        this.jda.shutdownNow();
        this.jda = null;
    }

}
