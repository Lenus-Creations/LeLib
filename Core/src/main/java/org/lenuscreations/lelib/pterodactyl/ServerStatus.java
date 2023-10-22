package org.lenuscreations.lelib.pterodactyl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServerStatus {

    OFFLINE("stop", "stopped"), STARTING(null, "starting"), ONLINE("start", "started"), STOPPING(null, "stopping"), ERROR(null, null);

    private final String signal;
    private final String name;

}
