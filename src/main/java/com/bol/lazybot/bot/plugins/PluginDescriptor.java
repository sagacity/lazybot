package com.bol.lazybot.bot.plugins;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PluginDescriptor {
    private String key;
}