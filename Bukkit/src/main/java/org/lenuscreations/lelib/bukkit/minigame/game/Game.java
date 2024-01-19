package org.lenuscreations.lelib.bukkit.minigame.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.lenuscreations.lelib.bukkit.minigame.MinigameAPI;
import org.lenuscreations.lelib.bukkit.minigame.game.team.Team;
import org.lenuscreations.lelib.bukkit.minigame.profile.GameProfile;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Game {

    private final MinigameAPI api;

    private final GameState state = GameState.WAITING;
    private final List<Team> teams = new ArrayList<>();
    private final List<GameProfile> players = new ArrayList<>();

    private boolean friendlyFire = false;

    public void addTeam(Team team) {
        teams.add(team);
    }

}
