package com.bol.lazybot;

import com.bol.lazybot.hipchat.installations.InstallationRepository;
import com.bol.lazybot.hipchat.installations.InstallationRepositoryPersistent;
import com.google.inject.AbstractModule;

public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(InstallationRepository.class).to(InstallationRepositoryPersistent.class);
    }
}
