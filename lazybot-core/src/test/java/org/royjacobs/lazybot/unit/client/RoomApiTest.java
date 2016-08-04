package org.royjacobs.lazybot.unit.client;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;
import org.royjacobs.lazybot.api.domain.Notification;
import org.royjacobs.lazybot.hipchat.client.RoomApiHttp;
import org.royjacobs.lazybot.utils.JacksonUtils;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.royjacobs.lazybot.unit.client.ApiUtils.getRoomApi;
import static org.royjacobs.lazybot.unit.client.ApiUtils.getValidOAuthToken;

public class RoomApiTest {
    private final MockWebServer server = new MockWebServer();
    private Notification receivedNotification;

    @Test
    public void sendMessage() throws IOException {
        server.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                final String body = request.getBody().readUtf8();
                receivedNotification = JacksonUtils.deserialize(body, Notification.class);
                return new MockResponse();
            }
        });
        server.start();

        final RoomApiHttp http = getRoomApi(server, (oauthId, oauthSecret) -> getValidOAuthToken());
        http.sendMessage("hello", null);
        assertThat(receivedNotification, is(new Notification("hello", null)));
    }
}
