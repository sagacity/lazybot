package org.royjacobs.lazybot.hipchat.installations;

import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginContext;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InstalledPlugin {
    private Plugin plugin;
    private PluginContext context;
}
