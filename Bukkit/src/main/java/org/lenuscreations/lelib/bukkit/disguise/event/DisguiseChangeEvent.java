package org.lenuscreations.lelib.bukkit.disguise.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.lenuscreations.lelib.bukkit.disguise.Disguise;

@Getter
@RequiredArgsConstructor
public class DisguiseChangeEvent extends Event {

    private final HandlerList handlers = new HandlerList();

    private final Disguise disguise;

}
