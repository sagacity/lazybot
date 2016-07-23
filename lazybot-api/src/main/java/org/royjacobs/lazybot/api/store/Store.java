package org.royjacobs.lazybot.api.store;

import java.util.Collection;
import java.util.Optional;

public interface Store<T> {
    Collection<T> findAll();
    Optional<T> get(final String key);
    void save(final String key, final T item);
    void delete(final String key);
    void clearAll();
}
