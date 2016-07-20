package com.bol.team5f.lazybot.commands;

import com.bol.team5f.lazybot.RequestContext;
import com.bol.team5f.lazybot.Command;
import com.bol.team5f.lazybot.api.Notification;

import java.util.List;

public class Eod implements Command {
    @Override
    public boolean handle(List<String> args, RequestContext rc) {
        rc.getApi().sendNotification(rc.getConfig().getRoomApi(), new Notification("bot", "text", "What up, g?"));
        return true;
    }

    @Override
    public String getName() {
        return "eod";
    }

    @Override
    public String getDescription() {
        return "Handles engineer on duty stuff";
    }
}
