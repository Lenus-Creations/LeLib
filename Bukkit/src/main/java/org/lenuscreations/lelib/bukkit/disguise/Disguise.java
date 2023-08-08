package org.lenuscreations.lelib.bukkit.disguise;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Disguise {

    private final UUID uuid;
    private final String username;

    private final UUID actualUUID;
    @Setter(AccessLevel.PROTECTED)
    private String actualName;

}
