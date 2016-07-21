package com.bol.lazybot;

import com.bol.lazybot.client.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import okhttp3.OkHttpClient;

public class ClientModule extends AbstractModule {
    @Override
    protected void configure() {
        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();
        bind(OkHttpClient.class).toInstance(httpClient);

        bind(OAuthApi.class).to(OAuthApiHttp.class);

        install(new FactoryModuleBuilder()
                .implement(RoomApi.class, RoomApiHttp.class)
                .build(RoomApiFactory.class));
    }
}
