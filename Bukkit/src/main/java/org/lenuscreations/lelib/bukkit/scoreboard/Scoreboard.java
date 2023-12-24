package org.lenuscreations.lelib.bukkit.scoreboard;

import com.google.common.collect.Lists;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Scoreboard {

    private final String title;
    private final List<ScoreboardLine> lines;

    private final org.bukkit.scoreboard.Scoreboard scoreboard;

    public Scoreboard(String title, ScoreboardLine[] lines) {
        this.title = title;
        this.lines = Lists.newArrayList(lines);

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        this.start();
    }

    private static AtomicInteger i = new AtomicInteger(0);
    public Scoreboard(String title, String[]... lines) {
        this(title, Arrays.stream(lines).map(l -> new ScoreboardLine(i.getAndIncrement(), l)).toArray(ScoreboardLine[]::new));
    }

    public void addLine(ScoreboardLine line) {
        lines.add(line);
    }

    public void addLine(String... text) {
        this.addLine(new ScoreboardLine(this.lines.size(), text));
    }

    public void addLine(int index, ScoreboardLine line) {
        lines.add(index, line);
    }

    public void addLine(int index, String... text) {
        this.addLine(index, new ScoreboardLine(index, text));
    }

    public void removeLine(int index) {
        lines.remove(index);
    }

    public void removeLine(ScoreboardLine line) {
        lines.remove(line);
    }

    private void start() {
        Objective objective = scoreboard.getObjective("lelib");
        if (objective == null) {
            objective = scoreboard.registerNewObjective("lelib", "dummy");
            objective.setDisplayName(title);
        }

        for (int i = 0; i < lines.size(); i++) {
            ScoreboardLine line = lines.get(i);
            line.run(this);
        }
    }

}
