package com.bol.team5f.lazybot;

import com.bol.team5f.lazybot.api.Notification;

import java.util.List;
import java.util.stream.Collectors;

class HelpCommand implements Command {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Gets help";
    }

    @Override
    public boolean handle(List<String> args, RequestContext rc) {
        final String names = rc.getCommands().stream().map(Command::getName).sorted().collect(Collectors.joining(", "));
        rc.getApi().sendNotification(rc.getConfig().getRoomApi(), new Notification("bot", "text", "(sadpanda) What? Available commands: " + names));
        return true;
    }
}
