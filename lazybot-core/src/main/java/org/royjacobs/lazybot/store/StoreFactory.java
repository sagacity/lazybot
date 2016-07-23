package org.royjacobs.lazybot.store;

import org.royjacobs.lazybot.api.store.Store;

public interface StoreFactory {
    <T> Store<T> get(final String key, final Class<T> clazz);
}
