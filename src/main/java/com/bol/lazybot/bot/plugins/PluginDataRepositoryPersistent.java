package com.bol.lazybot.bot.plugins;

import com.bol.lazybot.hipchat.client.RoomId;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.assistedinject.Assisted;
import lombok.extern.slf4j.Slf4j;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.bol.lazybot.utils.JacksonUtils.deserialize;
import static com.bol.lazybot.utils.JacksonUtils.serialize;

@Slf4j
public class PluginDataRepositoryPersistent implements PluginDataRepository {
    private final ObjectMapper objectMapper;
    private final File storeFile;
    private final ChronicleMap<String, String> store;

    @Inject
    public PluginDataRepositoryPersistent(final ObjectMapper objectMapper, @Assisted final RoomId roomId, @Assisted final PluginDescriptor pluginDescriptor) throws IOException {
        this.objectMapper = objectMapper;
        storeFile = new File("target/" + "plugindata-" + roomId.getValue() + "-" + pluginDescriptor.getKey() + ".db");
        this.store = getStore();
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
    }

    @Override
    public void clearAll() {
        if (!storeFile.delete()) {
            log.warn("Could not delete " + storeFile.getAbsolutePath() + "... will try on exit.");
            storeFile.deleteOnExit();
        }
    }

    private ChronicleMap<String,String> getStore() throws IOException {
        final ChronicleMapBuilder<String, String> builder = ChronicleMap.of(String.class, String.class).averageKeySize(32).averageValueSize(500).entries(10);

        try {
            return builder.createOrRecoverPersistedTo(storeFile);
        } catch (Exception e) {
            log.warn("Could not open plugin data. Recreating file.", e);
            return builder.createPersistedTo(storeFile);
        }
    }
}
