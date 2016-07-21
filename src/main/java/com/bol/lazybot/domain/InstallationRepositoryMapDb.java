package com.bol.lazybot.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import org.mapdb.DB;
import org.mapdb.Serializer;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static com.bol.lazybot.utils.JacksonUtils.deserialize;
import static com.bol.lazybot.utils.JacksonUtils.serialize;

public class InstallationRepositoryMapDb implements InstallationRepository {
    private final DB db;
    private final ObjectMapper objectMapper;
    private final ConcurrentMap<String, String> installations;

    @Inject
    public InstallationRepositoryMapDb(final DB db, final ObjectMapper objectMapper) {
        this.db = db;
        this.objectMapper = objectMapper;
        this.installations = db.hashMap("installations", Serializer.STRING, Serializer.STRING).createOrOpen();
    }

    @Override
    public ImmutableCollection<Installation> findAll() {
        final List<Installation> installations = this.installations.values()
                .stream()
                .map(json -> deserialize(objectMapper, json, Installation.class))
                .collect(Collectors.toList());
        return ImmutableList.copyOf(installations);
    }

    @Override
    public Optional<Installation> findByOAuthId(final String oauthId) {
        final String json = installations.get(oauthId);
        if (json == null) return Optional.empty();
        return Optional.of(deserialize(objectMapper, json, Installation.class));
    }

    @Override
    public void save(final Installation installation) {
        installations.put(installation.getOauthId(), serialize(objectMapper, installation));
        db.commit();
    }

    @Override
    public void delete(final String oauthId) {
        installations.remove(oauthId);
        db.commit();
    }
}
