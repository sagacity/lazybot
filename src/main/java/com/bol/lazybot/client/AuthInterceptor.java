package com.bol.lazybot.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class AuthInterceptor implements RequestInterceptor {
    private final String token;

    public AuthInterceptor(final String token) {
        this.token = token;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", "Bearer " + token);
    }
}

