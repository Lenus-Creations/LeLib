package org.lenuscreations.lelib.bukkit.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.lenuscreations.lelib.utils.TimeUtil;

import java.util.function.Consumer;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ChatInput {

    private final Consumer<ChatOutput> outputConsumer;
    private long expireAfter = -1L;

    protected final long startTime = System.currentTimeMillis();

}
