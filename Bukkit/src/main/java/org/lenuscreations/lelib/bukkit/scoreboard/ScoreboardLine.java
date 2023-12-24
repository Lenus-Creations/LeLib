package org.lenuscreations.lelib.bukkit.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ScoreboardLine {

    private final int index;
    private final String[] text;

    private boolean update = true;
    private boolean visible = true;

    private long textUpdateTime = 20L; // Default: 1 second

    void run(Scoreboard scoreboard) {
        org.bukkit.scoreboard.Scoreboard sb = scoreboard.getScoreboard();

        Objective objective = sb.getObjective("lelib");
        if (objective == null) {
            objective = sb.registerNewObjective("lelib", "dummy");
        }

        Objective finalObjective = objective;
        BukkitRunnable runnable = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (visible) {
                    finalObjective.getScore(text[i]).setScore(index + i);

                    if (i > text.length) {
                        i = 0;
                    } else {
                        i++;
                    }
                } else {
                    finalObjective.getScoreboard().resetScores(text[i]);
                }
            }
        };

        runnable.run();
        if (update) {
            runnable.runTaskTimer(AbstractPlugin.getInstance(), textUpdateTime, textUpdateTime);
        }
    }

}
