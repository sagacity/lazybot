package org.royjacobs.lazybot.unit.client;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;
import org.jetbrains.annotations.NotNull;
import org.royjacobs.lazybot.config.HipChatConfig;
import org.royjacobs.lazybot.hipchat.client.LoggingInterceptor;
import org.royjacobs.lazybot.hipchat.client.OAuthApi;
import org.royjacobs.lazybot.hipchat.client.RoomApiHttp;
import org.royjacobs.lazybot.hipchat.client.dto.RequestTokenResponse;
import org.royjacobs.lazybot.api.domain.Installation;

class ApiUtils {
    private final static Installation installation = Installation.builder().oauthId("oauthId").oauthSecret("oauthSecret").roomId("roomId").build();

    static RequestTokenResponse getValidOAuthToken() {
        final RequestTokenResponse response = new RequestTokenResponse();
        response.setAccessToken("accessToken");
        return response;
    }

    @NotNull
    static RoomApiHttp getRoomApi(MockWebServer server, OAuthApi oAuthApi) {
        final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();
        final HipChatConfig hipChatConfig = new HipChatConfig();
        hipChatConfig.setHipChatApiUrl(server.url("").toString());
        return new RoomApiHttp(client, hipChatConfig, oAuthApi, installation);
    }
}
