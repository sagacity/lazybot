package com.bol.lazybot.hipchat.client;

public interface RoomApi {
    void sendNotification(final Notification notification);
    void createWebhook(final CreateWebhookRequest request);
}
