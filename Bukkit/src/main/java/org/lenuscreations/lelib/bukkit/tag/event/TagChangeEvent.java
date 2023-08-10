package org.lenuscreations.lelib.bukkit.tag.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;
import org.lenuscreations.lelib.bukkit.tag.TagData;

@Getter
@RequiredArgsConstructor
public class TagChangeEvent extends Event {

    private final HandlerList handlers = new HandlerList();

    private final TagData tagData;
    @Nullable
    private final TagData previousTagData;

}
