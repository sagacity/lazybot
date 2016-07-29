package org.royjacobs.lazybot.api.store;

public interface StoreFactory {
    <T> Store<T> get(final String key, final Class<T> clazz);
}
