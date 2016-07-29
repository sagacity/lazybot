package org.royjacobs.lazybot.unit.bot;

import org.junit.Test;
import org.royjacobs.lazybot.api.plugins.PluginContext;
import org.royjacobs.lazybot.bot.DefaultPluginContextBuilder;
import org.royjacobs.lazybot.config.PluginConfig;
import org.royjacobs.lazybot.data.*;
import org.royjacobs.lazybot.api.domain.Installation;
import org.royjacobs.lazybot.testing.TestRoomApi;
import org.royjacobs.lazybot.testing.TestStoreFactory;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DefaultPluginContextBuilderTest {
    private final PluginConfig pluginConfig = new PluginConfig();
    private final Map<String, Object> pluginConfigMap = new HashMap<>();
    private final TestRoomApi roomApi = new TestRoomApi();
    private final TestStoreFactory storeFactory = new TestStoreFactory();

    @Test
    public void basicDataIsSet() {
        final PluginContext context = getBuilder().buildContext(new TestPlugin("foo"), Installation.builder().roomId("R1").build());
        assertThat(context.getRoomId(), is("R1"));
        assertThat(context.getRoomApi(), is(roomApi));
    }

    @Test
    public void pluginDataIsReadCorrectly() {
        pluginConfigMap.put("foo", new TestPluginConfigData("hello"));
        pluginConfig.setParameters(pluginConfigMap);

        final PluginContext context = getBuilder().buildContext(new TestPlugin("foo"), Installation.builder().roomId("R1").build());
        assertThat(((TestPluginConfigData)(context.getConfigData())).getData(), is("hello"));
    }

    @Test
    public void storesAreInitialized() {
        final PluginContext context = getBuilder().buildContext(new TestPlugin("foo"), Installation.builder().roomId("R1").build());
        final PluginContext context2 = getBuilder().buildContext(new TestPlugin("foo"), Installation.builder().roomId("R2").build());

        // Same key should give different values
        context.getRoomStore().save("key", new TestPluginRoomData("value"));
        context2.getRoomStore().save("key", new TestPluginRoomData("value2"));
        assertThat(((TestPluginRoomData)context.getRoomStore().get("key").get()).getData(), is("value"));
        assertThat(((TestPluginRoomData)context2.getRoomStore().get("key").get()).getData(), is("value2"));

        // Same key should give same values
        context.getGlobalStore().save("key", new TestPluginGlobalData("value"));
        context2.getGlobalStore().save("key", new TestPluginGlobalData("value2"));
        assertThat(((TestPluginGlobalData)context.getGlobalStore().get("key").get()).getData(), is("value2"));
        assertThat(((TestPluginGlobalData)context2.getGlobalStore().get("key").get()).getData(), is("value2"));
    }

    @Test
    public void canBuildPluginWithoutConfigOrStorage() {
        getBuilder().buildContext(new TestPlugin("empty"), Installation.builder().roomId("R1").build());
    }

    private DefaultPluginContextBuilder getBuilder() {
        return new DefaultPluginContextBuilder(pluginConfig, installation -> roomApi, storeFactory);
    }
}
