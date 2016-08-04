package org.royjacobs.lazybot.hipchat.client;

import org.royjacobs.lazybot.config.HipChatConfig;
import okhttp3.*;
import org.royjacobs.lazybot.hipchat.client.dto.RequestTokenResponse;
import org.royjacobs.lazybot.utils.JacksonUtils;

import javax.inject.Inject;

import java.io.IOException;
import java.util.stream.Collectors;

public class OAuthApiHttp implements OAuthApi {
    private final OkHttpClient httpClient;
    private final HipChatConfig hipChatConfig;
    private final String scope;

    @Inject
    public OAuthApiHttp(final OkHttpClient httpClient, final HipChatConfig hipChatConfig) {
        this.httpClient = httpClient;
        this.hipChatConfig = hipChatConfig;
        this.scope = hipChatConfig.getScopes().stream().collect(Collectors.joining("+"));
    }

    @Override
    public RequestTokenResponse requestToken(final String oauthId, final String oauthSecret) throws IOException {
        final Request request = new Request.Builder()
                .header("Authorization", Credentials.basic(oauthId, oauthSecret))
                .url(hipChatConfig.getRequestTokenUrl())
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "grant_type=client_credentials&scope=" + scope))
                .build();

        try (final Response response = httpClient.newCall(request).execute()) {
            return JacksonUtils.deserialize(response.body().string(), RequestTokenResponse.class);
        }
    }
}
