package org.royjacobs.lazybot.hipchat.installations;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.royjacobs.lazybot.bot.VariableCombiner;

import java.util.Set;

@Value
@Builder
public class InstallationContext {
    @Singular
    private Set<InstalledPlugin> plugins;

    private final VariableCombiner variableCombiner;
}
