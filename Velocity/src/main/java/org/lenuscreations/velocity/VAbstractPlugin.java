package org.lenuscreations.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.lenuscreations.velocity.command.CommandHandler;
import org.lenuscreations.velocity.command.test.TestCmd;
import org.slf4j.Logger;

@Getter
public abstract class VAbstractPlugin {

    @Getter
    private static VAbstractPlugin instance;

    private final ProxyServer server;
    private final Logger logger;

    /*
     * Create your own constructor exactly like this.
     */
    @Inject
    public VAbstractPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        instance = this;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

    }
}
