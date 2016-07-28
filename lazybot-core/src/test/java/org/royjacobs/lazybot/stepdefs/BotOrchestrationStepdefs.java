package org.royjacobs.lazybot.stepdefs;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.Data;
import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.domain.RoomMessage;
import org.royjacobs.lazybot.api.domain.RoomMessageItem;
import org.royjacobs.lazybot.api.domain.RoomMessageItemData;
import org.royjacobs.lazybot.api.plugins.PluginContext;
import org.royjacobs.lazybot.bot.BotOrchestrationService;
import org.royjacobs.lazybot.data.*;
import org.royjacobs.lazybot.hipchat.installations.Installation;
import org.royjacobs.lazybot.hipchat.installations.InstallationContext;
import org.royjacobs.lazybot.hipchat.installations.InstalledPlugin;
import org.royjacobs.lazybot.hipchat.server.install.InstallationHandler;
import org.royjacobs.lazybot.hipchat.server.install.dto.InstalledInformation;
import org.royjacobs.lazybot.hipchat.server.validator.HipChatRequestValidator;
import org.royjacobs.lazybot.hipchat.server.webhooks.RoomMessageHandler;
import org.royjacobs.lazybot.utils.JacksonUtils;
import ratpack.http.Request;
import ratpack.registry.Registry;
import ratpack.service.StartEvent;
import ratpack.service.StopEvent;
import ratpack.test.embed.EmbeddedApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class BotOrchestrationStepdefs {
    private TestPluginContextBuilder pluginContextBuilder = new TestPluginContextBuilder();
    private TestPluginProvider pluginProvider = new TestPluginProvider(() -> new TestPlugin("foo"), () -> new TestPlugin("bar"));
    private TestStore<Installation> store = new TestStore<>();
    private TestCommandDispatcher commandDispatcher = new TestCommandDispatcher();

    private BotOrchestrationService service = createService();

    private BotOrchestrationService createService() {
        return new BotOrchestrationService(
                pluginContextBuilder,
                store,
                pluginProvider,
                commandDispatcher
        );
    }

    private List<Installation> givenInstallations;

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

    @Given("^the following \"installed information\" coming in from HipChat$")
    public void theFollowingInstalledInformationComingInFromHipChat(List<InstalledInformation> installedInformations) throws Exception {
        for (InstalledInformation installedInformation : installedInformations) {
            EmbeddedApp.fromHandlers(chain -> chain
                    .register(r -> r.add(service))
                    .post(new InstallationHandler())
            )
                    .test(client -> client.request(r -> r
                            .body(b -> b.type("application/json").text(JacksonUtils.serialize(installedInformation)))
                            .post()));
        }
    }

    @Then("^The following installations should be registered$")
    public void theFollowingInstallationsShouldBeRegistered(List<Installation> installations) throws Throwable {
        assertThat(store.findAll().size(), is(installations.size()));

        for (Installation expected : installations) {
            final Optional<Installation> actual = store.get(expected.getOauthId());
            assertThat(actual.isPresent(), is(true));
            assertThat(actual.get(), is(expected));
        }
    }

    @When("^there is an unregistration for oauthId (\\S+) coming in from HipChat$")
    public void thereIsAnUnregistrationForOauthIdComingInFromHipChat(String oauthId) throws Exception {
        EmbeddedApp.fromHandlers(chain -> chain
                .register(r -> r.add(service))
                .delete(":oauthid", new InstallationHandler())
        )
                .test(client -> client.delete(oauthId));
    }

    @And("^plugin \"foo\" is broken$")
    public void pluginIsBroken() {
        class CannotStartPlugin extends TestPlugin {
            private CannotStartPlugin(String key) {
                super(key);
            }

            @Override
            public void onStart(PluginContext context) {
                throw new UnsupportedOperationException();
            }
        }

        // Replace plugin provider
        pluginProvider = new TestPluginProvider(() -> new CannotStartPlugin("foo"), () -> new TestPlugin("bar"));
        service = createService();
    }

    @And("^every installation should contain (\\d+) plugin$")
    public void everyInstallationShouldContainPlugin(int pluginCount) {
        for (Installation installation : store.findAll()) {
            assertThat(service.getContext(installation).getPlugins().size(), is(pluginCount));
        }
    }

    @Data
    private class CucumberRoomMessage {
        private String oauthId;
        private String message;
    }

    @When("^the following messages come in from HipChat$")
    public void theFollowingMessagesComeInFromHipChat(List<CucumberRoomMessage> messages) throws Throwable {
        for (CucumberRoomMessage message : messages) {
            final RoomMessage roomMessage = new RoomMessage();
            final RoomMessageItem roomMessageItem = new RoomMessageItem();
            final RoomMessageItemData roomMessageItemData = new RoomMessageItemData();

            roomMessage.setOauthId(message.getOauthId());

            roomMessageItemData.setMessage(message.getMessage());
            roomMessageItem.setMessage(roomMessageItemData);
            roomMessage.setItem(roomMessageItem);

            EmbeddedApp.fromHandlers(chain -> chain
                    .register(r -> r.add(service))
                    .register(r -> r.add(new HipChatRequestValidator() {
                        @Override
                        public void validate(Request request, String oauthSecret) {
                        }
                    }))
                    .post(new RoomMessageHandler())
            )
                    .test(client -> client.request(r -> r
                            .body(b -> b.type("application/json").text(JacksonUtils.serialize(roomMessage)))
                            .post()));
        }
    }

    @Data
    private class CucumberCommand {
        private String roomId;
        private String command;
    }

    @Then("^the following commands are dispatched$")
    public void theFollowingCommandsAreDispatched(List<CucumberCommand> commands) {
        for (CucumberCommand expected : commands) {
            final List<Command> commandsForRoom = commandDispatcher.getDispatchedCommands().get(expected.getRoomId());
            final Optional<String> actual = commandsForRoom.stream().map(Command::getCommand).findFirst();

            assertThat(actual.isPresent(), is(true));
            assertThat(actual.get(), is(expected.getCommand()));
        }
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
