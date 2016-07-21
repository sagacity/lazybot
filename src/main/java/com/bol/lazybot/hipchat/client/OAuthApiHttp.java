package com.bol.lazybot.hipchat.client;

import com.bol.lazybot.config.HipChatConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import javax.inject.Inject;

import java.io.IOException;
import java.util.stream.Collectors;

import static com.bol.lazybot.utils.JacksonUtils.deserialize;

public class OAuthApiHttp implements OAuthApi {
    private final ObjectMapper objectMapper;
    private final OkHttpClient httpClient;
    private final String scope;

    @Inject
    public OAuthApiHttp(final ObjectMapper objectMapper, final OkHttpClient httpClient, final HipChatConfig hipChatConfig) {
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
        this.scope = hipChatConfig.getScopes().stream().collect(Collectors.joining("+"));
    }

    @Override
    public RequestTokenResponse requestToken(final String oauthId, final String oauthSecret) throws IOException {
        final Request request = new Request.Builder()
                .header("Authorization", Credentials.basic(oauthId, oauthSecret))
                .url("https://api.hipchat.com/v2/oauth/token")
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "grant_type=client_credentials&scope=" + scope))
                .build();

        final Response response = httpClient.newCall(request).execute();
        return deserialize(objectMapper, response.body().string(), RequestTokenResponse.class);
    }
}
