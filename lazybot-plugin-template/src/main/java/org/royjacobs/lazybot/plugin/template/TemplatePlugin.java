package org.royjacobs.lazybot.plugin.template;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.domain.Notification;
import org.royjacobs.lazybot.api.plugins.*;

import java.util.Objects;
import java.util.stream.Collectors;

import static org.royjacobs.lazybot.api.plugins.PluginMessageHandlingResult.FAILURE;
import static org.royjacobs.lazybot.api.plugins.PluginMessageHandlingResult.SUCCESS;

public class TemplatePlugin implements Plugin {
    private PluginContext context;
    private PublicVariables variables;

    public PluginDescriptor getDescriptor() {
        return PluginDescriptor.builder()
                .key("template")
                .build();
    }

    public void onStart(PluginContext context) {
        this.context = context;
        context.getPublicVariables().subscribe(pv -> variables = pv);
    }

    public void onStop(boolean unregistered) {
    }

    public PluginMessageHandlingResult onCommand(Command command) {
        if (command.getCommand().equalsIgnoreCase("template")) {
            if (Objects.equals(command.getArgs().get(0), "say")) {
                final String template = command.getArgs().subList(1, command.getArgs().size()).stream().collect(Collectors.joining(" "));

                final StrSubstitutor substitutor = new StrSubstitutor(variables.getVariables());
                final String replaced = substitutor.replace(template);
                context.getRoomApi().sendNotification(new Notification("bot", "text", replaced));

                return SUCCESS;
            }
        }
        return FAILURE;
    }
}
