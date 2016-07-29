package org.royjacobs.lazybot.unit.client;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;
import org.royjacobs.lazybot.config.HipChatConfig;
import org.royjacobs.lazybot.hipchat.client.LoggingInterceptor;
import org.royjacobs.lazybot.hipchat.client.OAuthApiHttp;
import org.royjacobs.lazybot.hipchat.client.dto.RequestTokenResponse;
import org.royjacobs.lazybot.utils.JacksonUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OAuthApiTest {
    private final MockWebServer server = new MockWebServer();

    @Test
    public void requestToken() throws IOException {
        final RequestTokenResponse fakeResponse = new RequestTokenResponse();
        fakeResponse.setAccessToken("token!");
        server.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                final String body = request.getBody().readUtf8();
                assertThat(body, is("grant_type=client_credentials&scope=scope1+scope2"));
                return new MockResponse().setResponseCode(200).setBody(JacksonUtils.serialize(fakeResponse));
            }
        });
        server.start();

        final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();
        final HipChatConfig hipChatConfig = new HipChatConfig();
        hipChatConfig.setHipChatApiUrl(server.url("").toString());
        hipChatConfig.setScopes(new HashSet<>(Arrays.asList("scope1", "scope2")));
        final OAuthApiHttp http = new OAuthApiHttp(client, hipChatConfig);
        final RequestTokenResponse response = http.requestToken("id", "secret");

        assertThat(response, is(fakeResponse));
    }
}
