package org.lenuscreations.lelib.pterodactyl.admin.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.lenuscreations.lelib.pterodactyl.admin.location.PteroLocation;

@ToString
@Getter
@AllArgsConstructor
public class PteroNode {

    private final int id;
    private final String name;
    private final String uuid;
    private final String description;

    private final PteroLocation location;

    private final boolean publicNode;
    private final boolean behindProxy;
    private final boolean maintenanceMode;

    private final String fqdn;
    private final String scheme;
    private final int memory;
    private final int memoryOverallocate;
    private final int disk;
    private final int diskOverallocate;
    private final int uploadSize;
    private final int daemonListen;
    private final int daemonSftp;
    private final String daemonBase;
    private final String createdAt;
    private final String updatedAt;

}
