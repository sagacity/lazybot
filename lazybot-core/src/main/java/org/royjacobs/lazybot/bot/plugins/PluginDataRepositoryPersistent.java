package org.royjacobs.lazybot.bot.plugins;

import org.royjacobs.lazybot.hipchat.client.dto.RoomId;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.inject.assistedinject.Assisted;
import lombok.extern.slf4j.Slf4j;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import org.royjacobs.lazybot.utils.JacksonUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class PluginDataRepositoryPersistent implements PluginDataRepository {
    private final ObjectMapper objectMapper;
    private final File storeFile;
    private ChronicleMap<String, String> store;

    @Inject
    public PluginDataRepositoryPersistent(final ObjectMapper objectMapper, @Assisted final RoomId roomId, @Assisted final PluginDescriptor pluginDescriptor) throws IOException {
        this.objectMapper = objectMapper;
        storeFile = new File("target/" + "plugindata-" + roomId.getValue() + "-" + pluginDescriptor.getKey() + ".db");
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        final String json = getStore().get(key);
        if (json == null) return Optional.empty();
        return Optional.of(JacksonUtils.deserialize(objectMapper, json, clazz));
    }

    @Override
    public <T> void save(String key, T data) {
        getStore().put(key, JacksonUtils.serialize(objectMapper, data));
    }

    @Override
    public void clearAll() {
        if (!storeFile.delete()) {
            log.warn("Could not delete " + storeFile.getAbsolutePath() + "... will try on exit.");
            storeFile.deleteOnExit();
        }
    }

    private ChronicleMap<String,String> getStore() {
        if (store != null) return store;

        final ChronicleMapBuilder<String, String> builder = ChronicleMap.of(String.class, String.class).averageKeySize(32).averageValueSize(500).entries(10);

        try {
            store = builder.createOrRecoverPersistedTo(storeFile);
            return store;
        } catch (Exception e) {
            log.warn("Could not open plugin data. Recreating file.", e);
            try {
                store = builder.createPersistedTo(storeFile);
                return store;
            } catch (IOException e1) {
                log.warn("Couldn't even open plugin data when recreating the file!", e);
                throw Throwables.propagate(e);
            }
        }
    }
}
