package com.bol.lazybot.hipchat.installations;

import com.bol.lazybot.bot.plugins.Plugin;
import com.bol.lazybot.bot.plugins.PluginContext;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InstalledPlugin {
    private Plugin plugin;
    private PluginContext context;
}
