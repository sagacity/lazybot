package org.royjacobs.lazybot.plugin.template;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.plugins.PublicVariables;
import org.royjacobs.lazybot.testing.PluginTester;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TemplatePluginTest {
    private final PluginTester<TemplatePlugin> tester = new PluginTester<>(TemplatePlugin::new);

    @Test
    public void canTemplateMessage() {
        tester.getPublicVariables().onNext(new PublicVariables(ImmutableMap.of("foo.oof", "bar")));
        tester.test(plugin -> plugin.onCommand(Command.of("/bot template say hello ${foo.oof} rocks")));
        assertThat(tester.getRoomApi().getLastNotification().getMessage(), is("hello bar rocks"));

        tester.getPublicVariables().onNext(new PublicVariables(ImmutableMap.of("foo.oof", "boop")));
        tester.test(plugin -> plugin.onCommand(Command.of("/bot template say hello ${foo.oof} really rocks")));
        assertThat(tester.getRoomApi().getLastNotification().getMessage(), is("hello boop really rocks"));
    }
}
