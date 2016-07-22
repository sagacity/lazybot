package org.royjacobs.lazybot.hipchat.installations;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class InstallationContext {
    private Installation installation;

    @Singular
    private Set<InstalledPlugin> plugins;
}
