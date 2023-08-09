package org.lenuscreations.lelib.bukkit.tag.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.lenuscreations.lelib.bukkit.tag.TagData;

@Getter
@RequiredArgsConstructor
public class TagClearEvent extends Event {

    private final HandlerList handlers = new HandlerList();

    private final TagData oldTagData;

}
