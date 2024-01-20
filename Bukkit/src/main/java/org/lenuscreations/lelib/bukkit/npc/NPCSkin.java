package org.lenuscreations.lelib.bukkit.npc;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.lenuscreations.lelib.bukkit.utils.MojangUtils;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class NPCSkin {

    private final String name;
    private final String value;
    private final String signature;

    public static NPCSkin fromName(String name) {
        Map<String, String> skin = MojangUtils.getProfileProperties(name, false).get(0);
        if (skin.get("name") != "textures") return null;

        return new NPCSkin(name, skin.get("value"), skin.get("signature"));
    }

    public static NPCSkin fromUUID(UUID uuid) {
        String name = MojangUtils.getNameFromUUID(uuid);
        Map<String, String> skin = MojangUtils.getProfileProperties(uuid, false).get(0);
        if (skin.get("name") != "textures") return null;

        return new NPCSkin(name, skin.get("value"), skin.get("signature"));
    }

}
