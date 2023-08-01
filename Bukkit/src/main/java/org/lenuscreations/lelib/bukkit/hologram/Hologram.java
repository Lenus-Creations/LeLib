package org.lenuscreations.lelib.bukkit.hologram;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;

@Deprecated
@Data
@RequiredArgsConstructor
public class Hologram {

    private final String title;
    private final Location location;

    public Hologram(String title, float x, float y, float z, World world) {
        this(title, new Location(world, x, y, z));
    }

}
