package org.lenuscreations.lelib.pterodactyl.admin.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.lenuscreations.lelib.pterodactyl.ServerStatus;
import org.lenuscreations.lelib.pterodactyl.admin.PteroAdmin;
import org.lenuscreations.lelib.pterodactyl.admin.node.PteroNode;
import org.lenuscreations.lelib.pterodactyl.client.PteroClient;

@ToString
@Getter
@AllArgsConstructor
public class PteroServer {

    private final PteroAdmin admin;

    private final PteroClient user;
    private final PteroNode node;

    private int id;
    private String uuid;
    private String identifier;
    private String name;
    private String description;

    private ServerStatus status;

    private boolean suspended;

    private int memoryLimit;
    private int swapLimit;
    private int diskLimit;
    private int ioLimit;
    private int cpuLimit;

    private int databaseLimit;
    private int allocationLimit;
    private int backupsLimit;

    private String createdAt;
    private String updatedAt;

}
