package com.bol.lazybot;

import com.bol.lazybot.domain.InstallationRepository;
import com.bol.lazybot.domain.InstallationRepositoryMapDb;
import com.google.inject.AbstractModule;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;

public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        DB db = DBMaker
                .fileDB(new File("target/lazybot.db"))
                //.memoryDB()
                .transactionEnable()
                .make();

        bind(InstallationRepository.class).to(InstallationRepositoryMapDb.class);
        bind(DB.class).toInstance(db);
    }
}
