package com.bol.lazybot.domain;

import com.bol.lazybot.client.RoomId;
import com.bol.lazybot.plugins.PluginDescriptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.assistedinject.Assisted;
import org.mapdb.DB;
import org.mapdb.Serializer;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import static com.bol.lazybot.utils.JacksonUtils.deserialize;
import static com.bol.lazybot.utils.JacksonUtils.serialize;

public class PluginDataRepositoryMapDb implements PluginDataRepository {
    private final DB db;
    private final ObjectMapper objectMapper;
    private final String storeKey;
    private final ConcurrentMap<String, String> store;

    @Inject
    public PluginDataRepositoryMapDb(final DB db, final ObjectMapper objectMapper, @Assisted final RoomId roomId, @Assisted final PluginDescriptor pluginDescriptor) {
        this.db = db;
        this.objectMapper = objectMapper;
        this.storeKey = "plugindata-" + roomId.getValue() + "-" + pluginDescriptor.getKey();
        this.store = getMap().createOrOpen();
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        final String json = store.get(key);
        if (json == null) return Optional.empty();
        return Optional.of(deserialize(objectMapper, json, clazz));
    }

    @Override
    public <T> void save(String key, T data) {
        store.put(key, serialize(objectMapper, data));
        db.commit();
    }

    @Override
    public void clearAll() {
        // TODO: MapDB cannot remove entire maps yet, sigh
        getMap().createOrOpen().clear();
        db.commit();
    }

    private DB.HashMapMaker<String, String> getMap() {
        return db.hashMap(storeKey, Serializer.STRING, Serializer.STRING);
    }
}
