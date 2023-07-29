package org.lenuscreations.lelib.bukkit.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.lenuscreations.lelib.utils.TimeUtil;

@Data
@AllArgsConstructor
public class ChatOutput {

    private Player player;
    private String text;
    private long timeUsed;

    public String getTimeUsedText() {
        return TimeUtil.secondsToFormattedString((int) (timeUsed / 100L));
    }

}
