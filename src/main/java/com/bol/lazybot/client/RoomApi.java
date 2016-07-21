package com.bol.lazybot.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface RoomApi {
    @RequestLine("POST /v2/room/{room_id}/notification")
    @Headers("Content-Type: application/json")
    void sendNotification(@Param("room_id") String roomId, Notification notification);
}
