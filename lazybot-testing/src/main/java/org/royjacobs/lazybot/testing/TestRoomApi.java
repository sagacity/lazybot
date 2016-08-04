package org.royjacobs.lazybot.testing;

import lombok.Getter;
import org.royjacobs.lazybot.api.domain.Notification;
import org.royjacobs.lazybot.api.hipchat.RoomApi;

import java.util.ArrayList;
import java.util.List;

public class TestRoomApi implements RoomApi {
    @Getter
    private final List<Notification> notifications = new ArrayList<>();

    @Getter
    private final List<String> topics = new ArrayList<>();

    @Override
    public void sendNotification(Notification notification) {
        notifications.add(notification);
    }

    @Override
    public void setTopic(String topic) {
        topics.add(topic);
    }

    public Notification getLastNotification() {
        return notifications.get(notifications.size() - 1);
    }
}
