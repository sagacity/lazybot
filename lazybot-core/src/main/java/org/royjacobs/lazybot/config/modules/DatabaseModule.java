package org.royjacobs.lazybot.config.modules;

import org.royjacobs.lazybot.hipchat.installations.InstallationRepository;
import org.royjacobs.lazybot.hipchat.installations.InstallationRepositoryPersistent;
import com.google.inject.AbstractModule;

public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(InstallationRepository.class).to(InstallationRepositoryPersistent.class);
    }
}
