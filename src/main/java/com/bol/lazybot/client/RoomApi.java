package com.bol.lazybot.client;

public interface RoomApi {
    /*@RequestLine("POST /v2/room/{room_id}/notification")
    @Headers("Content-Type: application/json")
    void sendNotification(@Param("room_id") String roomId, Notification notification);*/
    void sendNotification(Notification notification);
}
