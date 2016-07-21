package com.bol.lazybot.plugins.eod;

import com.bol.lazybot.client.Notification;
import com.bol.lazybot.plugins.Plugin;
import com.bol.lazybot.plugins.PluginContext;
import com.bol.lazybot.plugins.PluginDescriptor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class EodPlugin implements Plugin {
    private PluginContext context;

    @Override
    public void onStart(final PluginContext context) {
        this.context = context;
        context.getRoomApi().sendNotification(new Notification("eod", "text", "(hodor) Hodor"));

        final Optional<String> abc = context.getRepository().get("abc", String.class);
        if (abc.isPresent()) log.info("YAY! ABC IS: " + abc.get());
        else context.getRepository().save("abc", "yay");
    }

    @Override
    public void onStop(final boolean removed) {
        context.getRoomApi().sendNotification(new Notification("eod", "text", "Bye now"));
    }

    @Override
    public PluginDescriptor getDescriptor() {
        return PluginDescriptor.builder()
                .key("eod")
                .build();
    }
}
