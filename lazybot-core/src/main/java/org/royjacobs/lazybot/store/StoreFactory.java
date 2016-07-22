package org.royjacobs.lazybot.store;

public interface StoreFactory {
    <T> Store<T> get(final String key, final Class<T> clazz);
}
