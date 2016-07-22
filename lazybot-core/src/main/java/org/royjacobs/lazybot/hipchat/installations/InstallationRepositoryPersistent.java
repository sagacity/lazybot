package org.royjacobs.lazybot.hipchat.installations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import net.openhft.chronicle.map.ChronicleMap;
import org.royjacobs.lazybot.utils.JacksonUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InstallationRepositoryPersistent implements InstallationRepository {
    private final ObjectMapper objectMapper;
    private final ChronicleMap<String, String> store;

    @Inject
    public InstallationRepositoryPersistent(final ObjectMapper objectMapper) throws IOException {
        this.objectMapper = objectMapper;
        this.store = ChronicleMap.of(String.class, String.class).averageKeySize(32).averageValueSize(500).entries(5000).createOrRecoverPersistedTo(new File("target/installations.db"));
    }

    @Override
    public ImmutableCollection<Installation> findAll() {
        final List<Installation> installations = this.store.values()
                .stream()
                .map(json -> JacksonUtils.deserialize(objectMapper, json, Installation.class))
                .collect(Collectors.toList());
        return ImmutableList.copyOf(installations);
    }

    @Override
    public Optional<Installation> findByOAuthId(final String oauthId) {
        final String json = store.get(oauthId);
        if (json == null) return Optional.empty();
        return Optional.of(JacksonUtils.deserialize(objectMapper, json, Installation.class));
    }

    @Override
    public void save(final Installation installation) {
        store.put(installation.getOauthId(), JacksonUtils.serialize(objectMapper, installation));
    }

    @Override
    public void delete(final String oauthId) {
        store.remove(oauthId);
    }
}
