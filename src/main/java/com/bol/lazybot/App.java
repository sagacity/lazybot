package com.bol.lazybot;

import com.bol.lazybot.server.capability.CapabilityHandler;
import com.bol.lazybot.server.installed.InstalledHandler;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import ratpack.func.Action;
import ratpack.func.Function;
import ratpack.guice.Guice;
import ratpack.registry.Registry;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfigBuilder;

@Slf4j
public class App {
    public static void main(String[] args) throws Exception {
        /*DB db = DBMaker
                .fileDB(new File("target/lazybot.db"))
                .transactionEnable()
                .make();
        ConcurrentMap<String, Object> map = db.hashMap("map", Serializer.STRING, Serializer.JAVA).createOrOpen();
        log.info("MAP: " + map.get("a"));
        map.put("a", 1234L);
        db.commit();
        db.close();*/

        /*final HipChatOAuth oauthApi = Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .requestInterceptor(new BasicAuthRequestInterceptor("db27710e-a9da-4bec-9e98-f38ad8958568", "Z1mOxdEKqzH5iNS5TSbJo50tODcD0gI4bMt98fvR"))
                .target(HipChatOAuth.class, "https://api.hipchat.com");

        final RequestTokenResponse response = oauthApi.requestToken("send_notification+view_messages+view_room");
        log.info("Response: {}", response);

        final HipChat api = Feign.builder()
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .requestInterceptor(new AuthInterceptor(response.getAccessToken()))
                .target(HipChat.class, "https://api.hipchat.com");

        api.sendNotification("2950589", new Notification("bot", "text", "(hodor) Hodor"));*/

        RatpackServer.start(s -> s
                .serverConfig(config())
                .registry(registry())
                .handlers(chain -> chain
                        .get("capabilities", CapabilityHandler.class)
                        .post("installed", InstalledHandler.class)
                )
        );
    }

    private static Function<Registry, Registry> registry() {
        return Guice.registry(b -> b
                .module(ServerModule.class)
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
