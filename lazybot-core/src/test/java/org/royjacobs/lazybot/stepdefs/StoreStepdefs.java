package org.royjacobs.lazybot.stepdefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.royjacobs.lazybot.api.store.Store;
import org.royjacobs.lazybot.config.DatabaseConfig;
import org.royjacobs.lazybot.store.PersistentStoreFactory;
import org.royjacobs.lazybot.store.StoreFactory;

import java.util.Optional;

import static java.lang.System.getProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@ScenarioScoped
public class StoreStepdefs {
    @Data
    @NoArgsConstructor
    private static class TestClass {
        String string;
        String uppercased;
    }

    private final StoreFactory storeFactory = new PersistentStoreFactory(new ObjectMapper(), getDatabaseConfig());

    private DatabaseConfig getDatabaseConfig() {
        final DatabaseConfig cfg = new DatabaseConfig();
        cfg.setFolder(getProperty("java.io.tmpdir"));
        return cfg;
    }

    private Store<TestClass> store;

    @Given("^I get a store$")
    public void iGetAStore() throws Throwable {
        store = storeFactory.get("test", TestClass.class);
    }

    @Given("^I get an empty store$")
    public void iGetAnEmptyStore() throws Throwable {
        iGetAStore();
        store.clearAll();
    }

    @And("^I store value \"([^\"]*)\" with key \"([^\"]*)\"$")
    public void iStoreValueWithKey(String value, String key) throws Throwable {
        final TestClass item = new TestClass();
        item.setString(value);
        item.setUppercased(value.toUpperCase());
        store.save(key, item);
    }

    @Then("^I should have stored (\\d+) items$")
    public void iShouldHaveStoredItems(int itemCount) throws Throwable {
        assertThat(store.findAll().size(), is(itemCount));
    }

    @And("^key \"([^\"]*)\" should have value \"([^\"]*)\"$")
    public void keyShouldHaveValue(String key, String value) throws Throwable {
        final Optional<TestClass> item = store.get(key);
        assertThat(item.isPresent(), is(true));
        assertThat(item.get().getString(), is(value));
        assertThat(item.get().getUppercased(), is(value.toUpperCase()));
    }

    @When("^I delete key \"([^\"]*)\"$")
    public void iDeleteKey(String key) throws Throwable {
        store.delete(key);
    }
}
