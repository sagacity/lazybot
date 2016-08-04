package org.royjacobs.lazybot.hipchat.client;

import com.google.common.base.Throwables;
import com.google.inject.assistedinject.Assisted;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.royjacobs.lazybot.api.domain.*;
import org.royjacobs.lazybot.api.hipchat.RoomApi;
import org.royjacobs.lazybot.config.HipChatConfig;
import org.royjacobs.lazybot.hipchat.server.glances.GlanceManager;
import org.royjacobs.lazybot.hipchat.client.dto.*;
import org.royjacobs.lazybot.hipchat.server.Paths;
import org.royjacobs.lazybot.utils.JacksonUtils;
import ratpack.server.ServerConfig;

import javax.inject.Inject;
import java.util.function.Consumer;

@Slf4j
public class RoomApiHttp implements RoomApi {
    private final OkHttpClient httpClient;
    private final ServerConfig serverConfig;
    private final HipChatConfig hipChatConfig;
    private final OAuthApi oAuthApi;
    private final GlanceManager glanceManager;
    private final Installation installation;
    private String token;

    @Inject
    public RoomApiHttp(
            final OkHttpClient httpClient,
            final ServerConfig serverConfig,
            final HipChatConfig hipChatConfig,
            final OAuthApi oAuthApi,
            final GlanceManager glanceManager,
            @Assisted final Installation installation) {
        this.httpClient = httpClient;
        this.serverConfig = serverConfig;
        this.hipChatConfig = hipChatConfig;
        this.oAuthApi = oAuthApi;
        this.glanceManager = glanceManager;
        this.installation = installation;
    }

    private void sendNotification(final Notification notification) {
        performRequest(new Request.Builder()
                .url(hipChatConfig.getRoomUrl(installation.getRoomId()) + "/notification")
                .post(RequestBody.create(MediaType.parse("application/json"), JacksonUtils.serialize(notification))));
    }

    @Override
    public void sendMessage(final String message, final Color color) {
        sendNotification(new Notification(message, color));
    }

    @Data
    @AllArgsConstructor
    private class Topic {
        String topic;
    }

    @Override
    public void setTopic(final String topic) {
        performRequest(new Request.Builder()
                .url(hipChatConfig.getRoomUrl(installation.getRoomId()) + "/topic")
                .put(RequestBody.create(MediaType.parse("application/json"), JacksonUtils.serialize(new Topic(topic)))));
    }

    private void createGlance(final CreateGlanceRequest createGlanceRequest) {
        performRequest(new Request.Builder()
                .url(hipChatConfig.getRoomUrl(installation.getRoomId()) + "/extension/glance/" + createGlanceRequest.getKey())
                .put(RequestBody.create(MediaType.parse("application/json"), JacksonUtils.serialize(createGlanceRequest))));
    }

    private void updateGlance(final UpdateGlanceRequest updateGlanceRequest) {
        performRequest(new Request.Builder()
                .url(hipChatConfig.getHipChatApiUrl() + "/v2/addon/ui/room/" + installation.getRoomId())
                .post(RequestBody.create(MediaType.parse("application/json"), JacksonUtils.serialize(updateGlanceRequest))));
    }

    @Override
    public Glance registerGlance(final String glanceKey, final Icon icon, final GlanceData initialData) {
        final String glancesUrl = serverConfig.getPublicAddress().resolve("/" + Paths.PATH_GLANCES).toString();

        glanceManager.registerGlance(installation.getOauthId(), glanceKey, initialData);

        final CreateGlanceRequest request = CreateGlanceRequest.builder()
                .name(new GlanceName("LazyBot"))
                .queryUrl(glancesUrl + "/" + installation.getOauthId() + "/" + glanceKey)
                .key(glanceKey)
                .icon(icon)
                .build();
        createGlance(request);

        final Consumer<GlanceData> setData = (glanceData) -> {
            final GlanceUpdate glanceUpdate = GlanceUpdate.builder()
                    .key(glanceKey)
                    .content(glanceManager.toContent(glanceData))
                    .build();

            updateGlance(UpdateGlanceRequest.builder().glance(glanceUpdate).build());
        };

        setData.accept(initialData);
        return new Glance() {
            @Override
            public void update(GlanceData data) {
                setData.accept(data);
            }

            @Override
            public String getKey() {
                return glanceKey;
            }
        };
    }

    @Override
    public void unregisterGlance(Glance glance) {
        glanceManager.unregisterGlance(installation.getOauthId(), glance.getKey());
    }

    private void performRequest(Request.Builder request) {
        request.header("Content-Type", "application/json");

        int retryCount = 5;
        while (retryCount-- != 0) {
            try {
                if (token == null) token = refreshOAuthToken();

                request.header("Authorization", "Bearer " + token);
                try (final Response response = httpClient.newCall(request.build()).execute()) {
                    if (response.code() < 400) return;

                    // 401 means the OAuth may be invalid, so let's refresh it
                    if (response.code() == 401) token = null;
                }
            } catch (Exception e) {
                log.error("Could not perform call", e);
            }
        }

        throw new RuntimeException("Could not perform call after retrying");
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
