package dev.grcq.v1_8_r3;

import dev.grcq.v1_8_r3.disguise.DisguiseHandler;
import lombok.Data;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class V1_8_R3 {

    @Getter
    private static final DisguiseHandler disguiseHandler;

    static {
        disguiseHandler = new DisguiseHandler();
    }

}
