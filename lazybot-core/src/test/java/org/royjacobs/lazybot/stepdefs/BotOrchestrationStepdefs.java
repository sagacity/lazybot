package org.royjacobs.lazybot.stepdefs;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.royjacobs.lazybot.bot.BotOrchestrationService;
import org.royjacobs.lazybot.data.TestPlugin;
import org.royjacobs.lazybot.data.TestPluginContextBuilder;
import org.royjacobs.lazybot.data.TestPluginProvider;
import org.royjacobs.lazybot.data.TestStore;
import org.royjacobs.lazybot.hipchat.installations.Installation;
import org.royjacobs.lazybot.hipchat.installations.InstallationContext;
import org.royjacobs.lazybot.hipchat.installations.InstalledPlugin;
import ratpack.registry.Registry;
import ratpack.service.StartEvent;
import ratpack.service.StopEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class BotOrchestrationStepdefs {
    private TestPluginContextBuilder pluginContextBuilder;
    private TestPluginProvider pluginProvider;
    private TestStore<Installation> store;

    private BotOrchestrationService service;
    private List<Installation> givenInstallations;

    @Before
    public void before() {
        pluginContextBuilder = new TestPluginContextBuilder();
        pluginProvider = new TestPluginProvider(() -> new TestPlugin("foo"), () -> new TestPlugin("bar"));
        store = new TestStore<>();

        service = new BotOrchestrationService(
                pluginContextBuilder,
                store,
                pluginProvider,
                (plugins1, command) -> null
        );
    }

    @Given("^the following installations are registered$")
    public void theFollowingInstallationsAreRegistered(List<Installation> installations) throws IOException {
        givenInstallations = installations;

        for (Installation installation : installations) {
            service.registerInstallation(installation);
        }
    }

    @Given("^the registered installations are unregistered$")
    public void theRegisteredInstallationsAreUnregistered() throws IOException {
        for (Installation installation : givenInstallations) {
            service.unregisterInstallation(installation);
        }
    }

    @Then("^the installation store should contain (\\d+) installations$")
    public void theInstallationStoreShouldContainInstallations(int numInstallations) {
        assertThat(store.findAll().size(), is(numInstallations));
    }

    private class FakeStartEvent implements StartEvent {
        @Override
        public Registry getRegistry() {
            return null;
        }

        @Override
        public boolean isReload() {
            return false;
        }
    }

    @When("^the \"start\" event is received$")
    public void theStartEventIsReceived() throws IOException {
        service.onStart(new FakeStartEvent());
    }

    private class FakeStopEvent implements StopEvent {
        @Override
        public Registry getRegistry() {
            return null;
        }

        @Override
        public boolean isReload() {
            return false;
        }
    }

    @When("^the \"stop\" event is received$")
    public void theStopEventIsReceived() throws IOException {
        service.onStop(new FakeStopEvent());
    }

    @Then("^all registered installations should be started with the right context$")
    public void allExistingInstallationsShouldBeStartedWithTheRightContext() {
        final List<TestPlugin> createdPlugins = new ArrayList<>(pluginProvider.getCreatedPlugins());
        assertThat(createdPlugins.size(), is(givenInstallations.size() * 2)); // 2 plugins per installation

        for (Installation givenInstallation : givenInstallations) {
            final Optional<Installation> installation = service.getActiveInstallationByOauthId(givenInstallation.getOauthId());
            assertThat(installation.isPresent(), is(true));

            installation.ifPresent(i -> {
                final InstallationContext context = service.getContext(i);
                assertThat(context.getPlugins().size(), is(2));
                for (InstalledPlugin plugin : context.getPlugins()) {
                    assertThat(plugin.getContext().getRoomId(), is(givenInstallation.getRoomId()));

                    final TestPlugin createdPlugin = (TestPlugin) plugin.getPlugin();
                    assertThat(createdPlugin.getContext(), is(plugin.getContext()));
                    assertThat(createdPlugins.contains(createdPlugin), is(true));
                    createdPlugins.remove(createdPlugin);
                }
            });
        }

        // All created plugins should have been part of an installation
        assertThat(createdPlugins.size(), is(0));
    }

    @Then("^all registered installations should be stopped$")
    public void allRegisteredInstallationsShouldBeStopped() {
        for (Installation givenInstallation : givenInstallations) {
            final Optional<Installation> installation = service.getActiveInstallationByOauthId(givenInstallation.getOauthId());
            assertThat(installation.isPresent(), is(false));
        }

        // All plugins should have been stopped
        for (TestPlugin plugin : pluginProvider.getCreatedPlugins()) {
            assertThat(plugin.isStopped(), is(true));
            assertThat(plugin.isUnregistered(), is(false));
        }
    }

    @Then("^the installation store should be empty$")
    public void theInstallationStoreShouldBeEmpty() {
        assertThat(store.findAll().size(), is(0));
    }

    @Then("^the plugins should have received an unregister call$")
    public void thePluginsShouldHaveReceivedAnUnregisterCall() throws Throwable {
        for (TestPlugin plugin : pluginProvider.getCreatedPlugins()) {
            assertThat(plugin.isStopped(), is(true));
            assertThat(plugin.isUnregistered(), is(true));

            final TestStore roomStore = (TestStore) pluginContextBuilder.getContextFor(plugin).getRoomStore();
            assertThat(roomStore.getTimesClearCalled(), is(1));
        }
    }
}
