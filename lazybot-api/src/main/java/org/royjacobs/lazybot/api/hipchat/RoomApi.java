package org.royjacobs.lazybot.api.hipchat;

import org.royjacobs.lazybot.api.domain.Notification;

public interface RoomApi {
    void sendNotification(final Notification notification);
}
