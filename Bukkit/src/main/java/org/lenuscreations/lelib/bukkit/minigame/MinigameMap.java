package org.lenuscreations.lelib.bukkit.minigame;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Map;

public interface MinigameMap {

    World getWorld();

    Location getSpawn();

    Map<String, Location> getLocations();

    default void load() {

    }

    default void unload() {

    }

}
