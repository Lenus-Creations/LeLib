package org.lenuscreations.lelib.bukkit.minigame;

import org.lenuscreations.lelib.bukkit.minigame.profile.GameProfile;

import java.util.List;

public interface MinigameAPI {

    void onStart();

    void onStop();

    MinigameMap getMap();

    boolean autoEndGame();

    default void onPlayerJoin(GameProfile profile) {}

    default void onPlayerLeave(GameProfile profile) {}

    default void onPlayerDeath(GameProfile profile) {}

    default void onPlayerRespawn(GameProfile profile) {}
    default void onPlayerQuit(GameProfile profile) {}

    default void onPlayerChat(GameProfile profile, String message) {}



}
