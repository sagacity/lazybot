package org.royjacobs.lazybot;

import org.royjacobs.lazybot.bot.BotOrchestrationService;
import org.royjacobs.lazybot.config.DatabaseConfig;
import org.royjacobs.lazybot.config.HipChatConfig;
import org.royjacobs.lazybot.config.modules.ClientModule;
import org.royjacobs.lazybot.config.modules.DatabaseModule;
import org.royjacobs.lazybot.config.modules.PluginModule;
import org.royjacobs.lazybot.config.modules.ServerModule;
import org.royjacobs.lazybot.hipchat.server.capabilities.GetCapabilitiesHandler;
import org.royjacobs.lazybot.hipchat.server.install.InstallationHandler;
import org.royjacobs.lazybot.hipchat.server.webhooks.RoomMessageHandler;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import ratpack.func.Action;
import ratpack.func.Function;
import ratpack.guice.Guice;
import ratpack.registry.Registry;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfigBuilder;

import static org.royjacobs.lazybot.hipchat.server.Paths.PATH_CAPABILITIES;
import static org.royjacobs.lazybot.hipchat.server.Paths.PATH_INSTALL;
import static org.royjacobs.lazybot.hipchat.server.Paths.PATH_WEBHOOK_ROOM_MESSAGE;

@Slf4j
public class App {
    public static void main(String[] args) throws Exception {
        final RatpackServer server = createServer();
        server.start();
    }

    public static RatpackServer createServer() throws Exception {
        return RatpackServer.of(s -> s
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
                .require("/hipchat", HipChatConfig.class)
                .require("/db", DatabaseConfig.class);
    }
}
