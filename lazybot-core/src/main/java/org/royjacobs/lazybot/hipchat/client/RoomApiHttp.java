package org.royjacobs.lazybot.hipchat.client;

import com.google.common.base.Throwables;
import com.google.inject.assistedinject.Assisted;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.royjacobs.lazybot.api.domain.Notification;
import org.royjacobs.lazybot.api.hipchat.RoomApi;
import org.royjacobs.lazybot.config.HipChatConfig;
import org.royjacobs.lazybot.hipchat.installations.Installation;
import org.royjacobs.lazybot.utils.JacksonUtils;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
public class RoomApiHttp implements RoomApi {
    private final OkHttpClient httpClient;
    private final HipChatConfig hipChatConfig;
    private final OAuthApi oAuthApi;
    private final Installation installation;
    private String token;

    @Inject
    public RoomApiHttp(
            final OkHttpClient httpClient,
            final HipChatConfig hipChatConfig,
            final OAuthApi oAuthApi,
            @Assisted final Installation installation) {
        this.httpClient = httpClient;
        this.hipChatConfig = hipChatConfig;
        this.oAuthApi = oAuthApi;
        this.installation = installation;
    }

    @Override
    public void sendNotification(final Notification notification) {
        performRequest(new Request.Builder()
                .url(hipChatConfig.getRoomUrl(installation.getRoomId()) + "/notification")
                .post(RequestBody.create(MediaType.parse("application/json"), JacksonUtils.serialize(notification))));
    }

    private void performRequest(Request.Builder request) {
        request.header("Content-Type", "application/json");

        int retryCount = 5;
        while (retryCount-- != 0) {
            try {
                if (token == null) token = refreshOAuthToken();

                request.header("Authorization", "Bearer " + token);
                final Response response = httpClient.newCall(request.build()).execute();
                if (response.code() < 400) return;

                // 401 means the OAuth may be invalid, so let's refresh it
                if (response.code() == 401) token = null;
            } catch (IOException e) {
                log.error("Could not perform call", e);
            }
        }
    }

    private String refreshOAuthToken() {
        try {
            return oAuthApi.requestToken(installation.getOauthId(), installation.getOauthSecret()).getAccessToken();
        } catch (Exception e) {
            log.error("Could not get OAuth token!", e);
            throw Throwables.propagate(e);
        }
    }
}
