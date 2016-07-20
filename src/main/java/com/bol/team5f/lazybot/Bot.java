package com.bol.team5f.lazybot;

import com.bol.team5f.lazybot.api.AuthInterceptor;
import com.bol.team5f.lazybot.commands.Eod;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import com.bol.team5f.lazybot.api.HipChat;
import com.bol.team5f.lazybot.api.Notification;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Slf4j
public class Bot {
    private static volatile boolean running = true;
    private static CountDownLatch shutdownLatch = new CountDownLatch(1);

    public static void main(final String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                running = false;
                try {
                    shutdownLatch.await();
                } catch (InterruptedException e) {
                    log.error("Couldn't shut down properly. Oh well.", e);
                }
            }
        });


        new Bot().run();
    }

    private void run() throws Exception {
        try {
            log.info("Hey hey, it's LazyBot! Starting up...");

            Config config = new Config();

            final HipChat api = Feign.builder()
                    .logger(new Slf4jLogger())
                    .logLevel(Logger.Level.FULL)
                    .decoder(new GsonDecoder())
                    .encoder(new GsonEncoder())
                    .requestInterceptor(new AuthInterceptor(config.getApiToken()))
                    .target(HipChat.class, "https://api.hipchat.com");

            final Reflections reflections = new Reflections(getClass().getPackage().getName() + ".commands");
            final List<Command> commands = reflections.getSubTypesOf(Command.class).stream()
                    .map(clazz -> {
                        try {
                            return (Command) clazz.getConstructors()[0].newInstance();
                        } catch (Exception e) {
                            log.error("Could not create command", e);
                            return null;
                        }
                    })
                    .collect(Collectors.toList());
            final HelpCommand helpCommand = new HelpCommand();
            commands.add(helpCommand);

            final Xmpp xmpp = new Xmpp(config);
            xmpp.connect();

            final RequestContext requestContext = new RequestContext(xmpp.getMuc(), api, config, commands);
            xmpp.getMuc().addMessageListener(message -> {
                final String body = message.getBody();
                if (body == null || !body.startsWith("/bot")) return;

                try {
                    final List<String> args = Arrays.asList(StringUtils.split(body));
                    if (args.size() >= 2) {
                        final List<String> argsWithoutPrefix = args.subList(1, args.size());
                        for (Command command : commands) {
                            if (command.getName().equalsIgnoreCase(argsWithoutPrefix.get(0))) {
                                if (command.handle(argsWithoutPrefix, requestContext)) return;
                            }
                        }
                    }
                    helpCommand.handle(args, requestContext);
                } catch (Exception e) {
                    log.error("Could not handle incoming message", e);
                }
            });

            api.sendNotification(config.getRoomApi(), new Notification("bot", "text", "(happypanda) Hi, y'all!"));

            while (running) {
                Thread.sleep(100);
            }

            api.sendNotification(config.getRoomApi(), new Notification("bot", "text", "(sadpanda) Bye, y'all!"));

            xmpp.disconnect();
        } catch (Exception e) {
            log.error("Whoa, that didn't go as expected", e);
        } finally {
            shutdownLatch.countDown();
        }
    }
}
