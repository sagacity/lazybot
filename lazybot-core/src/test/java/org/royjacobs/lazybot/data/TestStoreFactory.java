package org.royjacobs.lazybot.data;

import org.royjacobs.lazybot.api.store.Store;
import org.royjacobs.lazybot.store.StoreFactory;

import java.util.HashMap;
import java.util.Map;

public class TestStoreFactory implements StoreFactory {
    Map<String, Store> stores = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> Store<T> get(String key, Class<T> clazz) {
        final Store existing = stores.get(key);
        if (existing != null) return (Store<T>)existing;

        final TestStore<T> store = new TestStore<>();
        stores.put(key, store);
        return store;
    }
}
