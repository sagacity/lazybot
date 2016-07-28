package org.royjacobs.lazybot.stepdefs;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.royjacobs.lazybot.api.plugins.PublicVariables;
import org.royjacobs.lazybot.bot.VariableCombiner;
import org.royjacobs.lazybot.data.TestPlugin;
import rx.Subscription;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PublicVariablesStepdefs {
    private Set<TestPlugin> plugins;
    private VariableCombiner variableCombiner = new VariableCombiner();
    private Subscription subscription;
    private PublicVariables currentVariables;

    @Before
    public void before() {
        this.plugins = new HashSet<>(Arrays.asList(new TestPlugin("foo"), new TestPlugin("bar")));
        this.plugins.forEach(plugin -> variableCombiner.register(plugin.getDescriptor().getPublicVariables()));
        subscription = variableCombiner.getCurrentVariables().subscribe(cur -> currentVariables = cur);
    }

    @After
    public void after() {
        variableCombiner.unregisterAll();
        if (subscription != null) subscription.unsubscribe();
    }

    @Given("^for plugin \"([^\"]*)\" I set variable \"([^\"]*)\" to \"([^\"]*)\"$")
    public void forPluginISetVariableTo(String pluginKey, String varKey, String varValue) {
        final TestPlugin plugin = plugins.stream().filter(p -> p.getDescriptor().getKey().equals(pluginKey)).findFirst().get();
        plugin.setVariable(varKey, varValue);
    }

    @Then("^the combined variables should be$")
    public void theCombinedVariablesShouldBe(List<List<String>> data) {
        for (List<String> kvp : data) {
            final String key = kvp.get(0);
            final String value = kvp.get(1);
            assertThat(currentVariables.getVariables().get(key), is(value));
        }
    }
}
