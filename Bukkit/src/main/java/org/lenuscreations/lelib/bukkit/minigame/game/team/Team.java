package org.lenuscreations.lelib.bukkit.minigame.game.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.ChatColor;
import org.lenuscreations.lelib.bukkit.minigame.profile.GameProfile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Team {

    private final String name;
    private final String displayName;
    private final ChatColor color;

    private final String prefix;
    private final String suffix;

    private final int maxPlayers;
    private final int minPlayers;

    private final List<GameProfile> players = new ArrayList<>();

    public Team(String name, String displayName, ChatColor color, int maxPlayers, int minPlayers) {
        this(name, displayName, color, null, null, maxPlayers, minPlayers);
    }

    public Team(String name, String displayName, ChatColor color, int maxPlayers) {
        this(name, displayName, color, null, null, maxPlayers, 0);
    }
}
