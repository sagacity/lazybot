package org.royjacobs.lazybot.data;

import org.royjacobs.lazybot.api.domain.Notification;
import org.royjacobs.lazybot.api.hipchat.RoomApi;

public class TestRoomApi implements RoomApi {
    @Override
    public void sendNotification(Notification notification) {
    }
}
