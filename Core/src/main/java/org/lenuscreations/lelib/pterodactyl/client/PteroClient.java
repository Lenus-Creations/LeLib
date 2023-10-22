package org.lenuscreations.lelib.pterodactyl.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.lenuscreations.lelib.pterodactyl.client.server.ClientServer;

@AllArgsConstructor
public class PteroClient {

    @Getter
    private final String url;
    @Getter
    private final String token;

    public ClientServer getServer(String id) {
        return null;
    }

}
