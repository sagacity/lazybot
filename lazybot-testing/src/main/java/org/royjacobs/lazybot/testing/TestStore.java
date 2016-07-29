package org.royjacobs.lazybot.testing;

import lombok.Getter;
import org.royjacobs.lazybot.api.store.Store;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TestStore<T> implements Store<T> {
    private Map<String, T> store = new HashMap<>();

    @Getter
    private int timesClearCalled;

    @Override
    public Collection<T> findAll() {
        return store.values();
    }

    @Override
    public Optional<T> get(String key) {
        return Optional.ofNullable(store.get(key));
    }

    @Override
    public void save(String key, T item) {
        store.put(key, item);
    }

    @Override
    public void delete(String key) {
        store.remove(key);
    }

    @Override
    public void clearAll() {
        timesClearCalled++;
        store.clear();
    }
}
