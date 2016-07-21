package com.bol.lazybot.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.assistedinject.Assisted;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.inject.Inject;
import java.io.IOException;

import static com.bol.lazybot.utils.JacksonUtils.serialize;

@Slf4j
public class RoomApiHttp implements RoomApi {
    private final ObjectMapper objectMapper;
    private final OkHttpClient httpClient;
    private final String roomId;
    private final String token;

    @Inject
    public RoomApiHttp(final ObjectMapper objectMapper, final OkHttpClient httpClient, @Assisted final RoomId roomId, @Assisted final OAuthToken token) {
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
        this.roomId = roomId.getValue();
        this.token = token.getValue();
    }

    @Override
    public void sendNotification(final Notification notification) {
        final Request request = new Request.Builder()
                .url("https://api.hipchat.com/v2/room/" + roomId + "/notification")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .post(RequestBody.create(MediaType.parse("application/json"), serialize(objectMapper, notification)))
                .build();
        try {
            httpClient.newCall(request).execute();
        } catch (IOException e) {
            log.error("Could not perform call", e);
        }
    }
}
