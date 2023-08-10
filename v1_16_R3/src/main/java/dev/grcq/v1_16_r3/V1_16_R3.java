package dev.grcq.v1_16_r3;

import dev.grcq.v1_16_r3.tab.TabHandler;
import lombok.Getter;

public final class V1_16_R3 {

    @Getter
    private static final TabHandler tabHandler;

    static {
        tabHandler = new TabHandler();
    }

}
