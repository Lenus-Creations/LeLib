package dev.grcq.v1_8_r3;

import dev.grcq.v1_8_r3.disguise.DisguiseHandler;
import dev.grcq.v1_8_r3.tag.TagHandler;
import lombok.Data;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class V1_8_R3 {

    @Getter
    private static final DisguiseHandler disguiseHandler;
    @Getter
    private static final TagHandler tagHandler;

    static {
        disguiseHandler = new DisguiseHandler();
        tagHandler = new TagHandler();
    }

}
