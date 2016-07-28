package org.royjacobs.lazybot.unit.client;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;
import org.royjacobs.lazybot.api.domain.Notification;
import org.royjacobs.lazybot.config.HipChatConfig;
import org.royjacobs.lazybot.hipchat.client.RoomApiHttp;
import org.royjacobs.lazybot.hipchat.client.dto.RequestTokenResponse;
import org.royjacobs.lazybot.hipchat.installations.Installation;
import org.royjacobs.lazybot.utils.JacksonUtils;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RoomApiTest {
    private final MockWebServer server = new MockWebServer();
    private final Installation installation = Installation.builder().oauthId("oauthId").oauthSecret("oauthSecret").roomId("roomId").build();
    private Notification receivedNotification;

    @Test
    public void sendNotification() throws IOException {
        server.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                final String body = request.getBody().readUtf8();
                receivedNotification = JacksonUtils.deserialize(body, Notification.class);
                return new MockResponse();
            }
        });
        server.start();

        final OkHttpClient client = new OkHttpClient.Builder().build();
        final HipChatConfig hipChatConfig = new HipChatConfig();
        hipChatConfig.setHipChatApiUrl(server.url("").toString());
        final RoomApiHttp http = new RoomApiHttp(client, hipChatConfig, (oauthId, oauthSecret) -> getValidOAuthToken(), installation);

        final Notification notification = new Notification("bot", "text", "hello");
        http.sendNotification(notification);
        assertThat(receivedNotification, is(notification));
    }

    private RequestTokenResponse getValidOAuthToken() {
        final RequestTokenResponse response = new RequestTokenResponse();
        response.setAccessToken("accessToken");
        return response;
    }
}
