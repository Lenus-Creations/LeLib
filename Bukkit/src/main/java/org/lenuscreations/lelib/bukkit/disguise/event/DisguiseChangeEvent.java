package org.lenuscreations.lelib.bukkit.disguise.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.lenuscreations.lelib.bukkit.disguise.Disguise;

@Getter
@RequiredArgsConstructor
public class DisguiseChangeEvent extends Event {

    private final HandlerList handlerList = new HandlerList();

    private final Disguise disguise;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
