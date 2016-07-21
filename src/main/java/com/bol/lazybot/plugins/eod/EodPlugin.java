package com.bol.lazybot.plugins.eod;

import com.bol.lazybot.client.Notification;
import com.bol.lazybot.plugins.Plugin;
import com.bol.lazybot.plugins.PluginContext;

public class EodPlugin implements Plugin {
    @Override
    public void onStart(final PluginContext context) {
        context.getRoomApi().sendNotification(context.getRoomId(), new Notification("bot", "text", "(hodor) Hodor"));
    }
}
