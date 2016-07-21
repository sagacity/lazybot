package com.bol.lazybot;

import com.bol.lazybot.bot.BotOrchestrationService;
import com.bol.lazybot.hipchat.server.capabilities.GetCapabilitiesHandler;
import com.bol.lazybot.hipchat.server.install.InstallationHandler;
import com.bol.lazybot.hipchat.server.webhooks.RoomMessageHandler;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import ratpack.func.Action;
import ratpack.func.Function;
import ratpack.guice.Guice;
import ratpack.registry.Registry;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfigBuilder;

import static com.bol.lazybot.hipchat.server.Paths.PATH_CAPABILITIES;
import static com.bol.lazybot.hipchat.server.Paths.PATH_INSTALL;
import static com.bol.lazybot.hipchat.server.Paths.PATH_WEBHOOK_ROOM_MESSAGE;

@Slf4j
public class App {
    public static void main(String[] args) throws Exception {
        RatpackServer.start(s -> s
                .serverConfig(config())
                .registry(registry())
                .handlers(chain -> chain
                        .get(PATH_CAPABILITIES, GetCapabilitiesHandler.class)
                        .post(PATH_INSTALL, InstallationHandler.class)
                        .delete(PATH_INSTALL + "/:oauthid", InstallationHandler.class)
                        .post(PATH_WEBHOOK_ROOM_MESSAGE, RoomMessageHandler.class)
                )
        );
    }

    private static Function<Registry, Registry> registry() {
        return Guice.registry(b -> b
                .module(ClientModule.class)
                .module(ServerModule.class)
                .module(DatabaseModule.class)
                .module(PluginModule.class)
                .bind(BotOrchestrationService.class)
        );
    }

    private static Action<ServerConfigBuilder> config() {
        return c -> c
                .json(Resources.getResource("config.json"))
                .env("LAZYBOT_")
                .sysProps("lazybot.")
                .require("/hipchat", HipChatConfig.class);
    }
}
