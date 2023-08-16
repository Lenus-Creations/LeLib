package org.lenuscreations.lelib.bukkit.nick;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;

import java.util.UUID;

@Data
@AllArgsConstructor
public class NickData {

    public NickData(UUID uuid) {
        this(uuid, AbstractPlugin.getInstance().getNicknameHandler().getNickname(uuid).getNick());
    }

    private final UUID uuid;
    private final String nick;

    // more?

}
