package org.lenuscreations.lelib.bukkit.nick;

import lombok.Data;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lenuscreations.lelib.bukkit.AbstractPlugin;
import org.lenuscreations.lelib.bukkit.utils.StorageType;
import org.lenuscreations.lelib.file.Configuration;
import org.lenuscreations.lelib.file.FileHandler;
import org.lenuscreations.lelib.file.value.impl.ConfigurationChildren;
import org.lenuscreations.lelib.file.value.impl.StringValue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class NicknameHandler {

    private final List<NickData> nicknames;
    private StorageType storageType;

    @SneakyThrows
    public NicknameHandler(@NotNull StorageType type) {
        this.storageType = type;
        this.nicknames = new ArrayList<>();

        switch (type) {
            case FILE:
                File file = new File("nick.yml");
                if (!file.exists()) file.createNewFile();

                FileHandler fileHandler = new FileHandler("nick.yml", AbstractPlugin.getInstance().getClass());
                for (Configuration configuration : ((ConfigurationChildren) fileHandler.getConfig("nick").getValue()).getValue()) {
                    UUID uuid = UUID.fromString(configuration.getName());
                    String nick = ((StringValue) configuration.getValue()).getValue();

                    NickData nickData = new NickData(uuid, nick);
                    this.nick(nickData);
                }
                break;
            case DATABASE:
                throw new RuntimeException("Not yet implemented.");
            default:
                break;
        }
    }

    public NicknameHandler() {
        this(StorageType.NONE);
    }

    public void nick(NickData data) {

        switch (storageType) {
            case FILE:
                FileHandler fileHandler = new FileHandler("nick.yml", AbstractPlugin.getInstance().getClass());
                fileHandler.set(new Configuration(data.getUuid().toString(), new StringValue(data.getNick())));
                fileHandler.save(AbstractPlugin.getInstance().getClass());
                break;
            case DATABASE:
                break;
            default:
                break;
        }

        // todo: nick

        nicknames.add(data);
    }

    public boolean isNicked(UUID uuid) {
        return nicknames.stream().anyMatch(n -> n.getUuid().equals(uuid));
    }

    public boolean isNicked(Player player) {
        return this.isNicked(player.getUniqueId());
    }

    public NickData getNickname(UUID uuid) {
        return nicknames.stream().filter(n -> n.getUuid().toString().equals(uuid.toString())).findFirst().orElse(null);
    }
}
