package com.bol.lazybot.plugins.eod;

import com.bol.lazybot.client.Notification;
import com.bol.lazybot.plugins.Plugin;
import com.bol.lazybot.plugins.PluginContext;

public class EodPlugin implements Plugin {
    private PluginContext context;

    @Override
    public void onStart(final PluginContext context) {
        this.context = context;
        context.getRoomApi().sendNotification(new Notification("eod", "text", "(hodor) Hodor"));
    }

    @Override
    public void onStop() {
        context.getRoomApi().sendNotification(new Notification("eod", "text", "Bye now"));
    }
}
