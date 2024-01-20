package org.lenuscreations.lelib.bukkit.npc;

import dev.grcq.v1_8_r3.npc.NPC_1_8_R3;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class NPCData {

    @Getter
    private final int id;
    private final NPC_1_8_R3 npc_1_8_r3;

    public NPCData(int id, NPC_1_8_R3 npc) {
        this.id = id;
        this.npc_1_8_r3 = npc;
    }

    public NPC_1_8_R3 getNPC_1_8_R3() {
        return this.npc_1_8_r3;
    }

}
