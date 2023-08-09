package org.lenuscreations.lelib.bukkit.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
public class TagData {

    private final Player player;

    @Nullable
    private String prefix;
    @Nullable
    private String suffix;
    @Nullable
    private String colour;

}
