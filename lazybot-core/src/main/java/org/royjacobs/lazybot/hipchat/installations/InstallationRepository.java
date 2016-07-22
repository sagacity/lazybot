package org.royjacobs.lazybot.hipchat.installations;

import com.google.common.collect.ImmutableCollection;

import java.util.Optional;

public interface InstallationRepository {
    ImmutableCollection<Installation> findAll();
    Optional<Installation> findByOAuthId(final String oauthId);
    void save(final Installation installation);
    void delete(final String oauthId);
}
