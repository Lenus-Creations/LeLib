package dev.grcq.v1_12_r1;

import dev.grcq.v1_12_r1.disguise.DisguiseHandler;
import dev.grcq.v1_12_r1.tab.TabHandler;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class V1_12_R1 {

    @Getter
    private static final DisguiseHandler disguiseHandler;
    @Getter
    private static final TabHandler tabHandler;

    static {
        disguiseHandler = new DisguiseHandler();
        tabHandler = new TabHandler();
    }

}
