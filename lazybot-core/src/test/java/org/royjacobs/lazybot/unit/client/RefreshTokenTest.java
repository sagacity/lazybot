package org.royjacobs.lazybot.unit.client;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.royjacobs.lazybot.api.domain.Notification;
import org.royjacobs.lazybot.hipchat.client.RoomApiHttp;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.royjacobs.lazybot.unit.client.ApiUtils.getRoomApi;
import static org.royjacobs.lazybot.unit.client.ApiUtils.getValidOAuthToken;

public class RefreshTokenTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private final MockWebServer server = new MockWebServer();

    @Test
    public void tokenExpired() throws IOException {
        server.enqueue(new MockResponse().setResponseCode(401));
        server.enqueue(new MockResponse().setResponseCode(200));
        server.start();
        final RoomApiHttp http = getRoomApi(server, ((oauthId, oauthSecret) -> getValidOAuthToken()));
        http.sendNotification(new Notification("bot", "text", "hello"));
    }

    @Test
    public void tokenExpiredOAuthException() throws IOException {
        server.enqueue(new MockResponse().setResponseCode(401));
        server.enqueue(new MockResponse().setResponseCode(200));
        server.start();

        AtomicInteger requestCount = new AtomicInteger(0);
        final RoomApiHttp http = getRoomApi(server, ((oauthId, oauthSecret) -> {
            final int count = requestCount.addAndGet(1);
            switch (count) {
                case 1:
                    throw new RuntimeException(); // cannot refresh token the first time
                default:
                    return getValidOAuthToken();
            }
        }));
        http.sendNotification(new Notification("bot", "text", "hello"));
    }

    @Test
    public void tokenRemainsExpired() throws IOException {
        // Will retry 5 times
        server.enqueue(new MockResponse().setResponseCode(401));
        server.enqueue(new MockResponse().setResponseCode(401));
        server.enqueue(new MockResponse().setResponseCode(401));
        server.enqueue(new MockResponse().setResponseCode(401));
        server.enqueue(new MockResponse().setResponseCode(401));
        server.start();

        exception.expect(RuntimeException.class);
        final RoomApiHttp http = getRoomApi(server, ((oauthId, oauthSecret) -> getValidOAuthToken()));
        http.sendNotification(new Notification("bot", "text", "hello"));
    }
}
