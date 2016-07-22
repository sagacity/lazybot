package org.royjacobs.lazybot.config.modules;

import com.google.inject.AbstractModule;
import org.royjacobs.lazybot.store.PersistentStoreFactory;
import org.royjacobs.lazybot.store.StoreFactory;

public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(StoreFactory.class).to(PersistentStoreFactory.class);
    }
}
