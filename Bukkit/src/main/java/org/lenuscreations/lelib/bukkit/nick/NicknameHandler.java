package org.lenuscreations.lelib.bukkit.nick;

import lombok.Data;
import org.bukkit.entity.Player;
import org.lenuscreations.lelib.bukkit.utils.StorageType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class NicknameHandler {

    private final List<NickData> nicknames;
    private StorageType storageType;

    public NicknameHandler(StorageType type) {
        this.storageType = type;
        this.nicknames = new ArrayList<>();

        // TODO: load nicks from database and file to automatically apply upon joining.
        switch (type) {
            case FILE:
                break;
            case DATABASE:
                break;
            default:
                break;
        }
    }

    public NicknameHandler() {
        this(StorageType.NONE);
    }

    public void nick(NickData data) {



        nicknames.add(data);
    }

    public boolean isNicked(UUID uuid) {
        return nicknames.stream().anyMatch(n -> n.getUuid().equals(uuid));
    }

    public boolean isNicked(Player player) {
        return this.isNicked(player.getUniqueId());
    }

    public NickData getNickname(UUID uuid) {
        return null;
    }
}
