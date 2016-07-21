package com.bol.lazybot;

import com.bol.lazybot.client.OrchestrationService;
import com.bol.lazybot.server.capabilities.GetCapabilities;
import com.bol.lazybot.server.install.PostInstallation;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import ratpack.func.Action;
import ratpack.func.Function;
import ratpack.guice.Guice;
import ratpack.registry.Registry;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfigBuilder;

import static com.bol.lazybot.server.Paths.PATH_CAPABILITIES;
import static com.bol.lazybot.server.Paths.PATH_INSTALL;

@Slf4j
public class App {
    public static void main(String[] args) throws Exception {
        RatpackServer.start(s -> s
                .serverConfig(config())
                .registry(registry())
                .handlers(chain -> chain
                        .get(PATH_CAPABILITIES, GetCapabilities.class)
                        .post(PATH_INSTALL, PostInstallation.class)
                        .delete(PATH_INSTALL + "/:oauth-id", PostInstallation.class)
                )
        );
    }

    private static Function<Registry, Registry> registry() {
        return Guice.registry(b -> b
                .module(ServerModule.class)
                .module(DatabaseModule.class)
                .module(PluginModule.class)
                .bind(OrchestrationService.class)
        );
    }

    private static Action<ServerConfigBuilder> config() {
        return c -> c
                .json(Resources.getResource("config.json"))
                .env()
                .sysProps();
                //.require("/db", HikariConfig.class)
    }
}
