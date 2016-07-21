package com.bol.lazybot;

import com.bol.lazybot.domain.InstallationRepository;
import com.bol.lazybot.domain.InstallationRepositoryPersistent;
import com.google.inject.AbstractModule;

public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(InstallationRepository.class).to(InstallationRepositoryPersistent.class);
    }
}
