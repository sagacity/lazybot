package org.royjacobs.lazybot;

import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import org.royjacobs.lazybot.bot.BotOrchestrationService;
import org.royjacobs.lazybot.config.DatabaseConfig;
import org.royjacobs.lazybot.config.HipChatConfig;
import org.royjacobs.lazybot.config.PluginConfig;
import org.royjacobs.lazybot.config.modules.*;
import org.royjacobs.lazybot.hipchat.server.capabilities.GetCapabilitiesHandler;
import org.royjacobs.lazybot.hipchat.server.glances.GetGlancesHandler;
import org.royjacobs.lazybot.hipchat.server.install.InstallationHandler;
import org.royjacobs.lazybot.hipchat.server.webhooks.RoomMessageHandler;
import ratpack.func.Action;
import ratpack.func.Function;
import ratpack.guice.Guice;
import ratpack.registry.Registry;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfigBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.royjacobs.lazybot.hipchat.server.Paths.*;

@Slf4j
public class App {
    public static void main(String[] args) throws Exception {
        if (args.length > 1) {
            log.error("Please provide the location of a JSON config file on the command line, or leave empty.");
            return;
        }

        final RatpackServer server = createServer(args);
        server.start();
    }

    public static RatpackServer createServer(String[] args) throws Exception {
        return RatpackServer.of(s -> s
                .serverConfig(config(args))
                .registry(registry())
                .handlers(chain -> chain
                        .get(PATH_CAPABILITIES, GetCapabilitiesHandler.class)
                        .post(PATH_INSTALL, InstallationHandler.class)
                        .delete(PATH_INSTALL + "/:oauthid", InstallationHandler.class)
                        .post(PATH_WEBHOOK_ROOM_MESSAGE, RoomMessageHandler.class)
                        .get(PATH_GLANCES + "/:oauthid/:key", GetGlancesHandler.class)
                )
        );
    }

    private static Function<Registry, Registry> registry() {
        return Guice.registry(b -> b
                .module(ClientModule.class)
                .module(ServerModule.class)
                .module(DatabaseModule.class)
                .module(BotModule.class)
                .module(PluginModule.class)
                .bind(BotOrchestrationService.class)
        );
    }

    private static Action<ServerConfigBuilder> config(String[] args) {
        final Path baseDir = Paths.get("").toAbsolutePath();
        log.info("Using baseDir: " + baseDir);

        Action<ServerConfigBuilder> cfg = Action.from(c -> c
                .baseDir(baseDir)
                .json(Resources.getResource("config-core.json"))
        );

        if (args.length == 1) {
            final String filename = args[0];
            final Path configPath = Paths.get(baseDir.toString(), filename);
            if (!Files.exists(configPath)) {
                throw new UnsupportedOperationException("Could not find config file: " + configPath);
            }
            cfg = cfg.append(c -> c.json(configPath));
        }

        cfg = cfg.append(c -> c
                .env("LAZYBOT_")
                .sysProps("lazybot.")
                .require("/hipchat", HipChatConfig.class)
                .require("/db", DatabaseConfig.class)
                .require("/plugins", PluginConfig.class)
        );

        return cfg;
    }
}
