package org.royjacobs.lazybot.hipchat.client;

import org.royjacobs.lazybot.hipchat.client.dto.Notification;

public interface RoomApi {
    void sendNotification(final Notification notification);
}
