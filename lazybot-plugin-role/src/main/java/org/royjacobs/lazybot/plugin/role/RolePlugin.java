package org.royjacobs.lazybot.plugin.role;

import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.plugins.Plugin;
import org.royjacobs.lazybot.api.plugins.PluginContext;
import org.royjacobs.lazybot.api.plugins.PluginDescriptor;
import org.royjacobs.lazybot.api.plugins.PluginMessageHandlingResult;

import static org.royjacobs.lazybot.api.plugins.PluginMessageHandlingResult.NOT_INTENDED_FOR_THIS_PLUGIN;
import static org.royjacobs.lazybot.api.plugins.PluginMessageHandlingResult.SUCCESS;

public class RolePlugin implements Plugin {
    @Override
    public PluginDescriptor getDescriptor() {
        return PluginDescriptor.builder()
                .key("role")
                .roomDataClass(RolePluginRoomData.class)
                .build();
    }

    @Override
    public void onStart(PluginContext context) {

    }

    @Override
    public void onStop(boolean unregistered) {

    }

    @Override
    public PluginMessageHandlingResult onCommand(Command command) {
        if (!command.getCommand().equalsIgnoreCase("role")) return NOT_INTENDED_FOR_THIS_PLUGIN;
        return SUCCESS;
    }
}
