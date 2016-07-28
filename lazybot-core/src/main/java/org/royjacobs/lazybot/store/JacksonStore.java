package org.royjacobs.lazybot.store;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import org.royjacobs.lazybot.api.store.Store;
import org.royjacobs.lazybot.utils.JacksonUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class JacksonStore<T> implements Store<T> {
    private final ConcurrentMap<String, String> map;
    private final Class<T> clazz;

    JacksonStore(final ConcurrentMap<String, String> map, Class<T> clazz) {
        this.map = map;
        this.clazz = clazz;
    }

    public ImmutableCollection<T> findAll() {
        final List<T> items = map.values()
                .stream()
                .map(json -> JacksonUtils.deserialize(json, clazz))
                .collect(Collectors.toList());
        return ImmutableList.copyOf(items);
    }

    public Optional<T> get(final String key) {
        final String json = map.get(key);
        if (json == null) return Optional.empty();
        return Optional.of(JacksonUtils.deserialize(json, clazz));
    }

    public void save(final String key, final T item) {
        map.put(key, JacksonUtils.serialize(item));
    }

    public void delete(final String key) {
        map.remove(key);
    }

    public void clearAll() {
        map.clear();
    }
}
