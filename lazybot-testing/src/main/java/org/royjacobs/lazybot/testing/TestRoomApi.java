package org.royjacobs.lazybot.testing;

import lombok.Getter;
import org.royjacobs.lazybot.api.domain.Glance;
import org.royjacobs.lazybot.api.domain.GlanceData;
import org.royjacobs.lazybot.api.domain.Icon;
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

    @Override
    public Glance registerGlance(String glanceKey, Icon icon, GlanceData initialData) {
        return null;
    }

    @Override
    public void unregisterGlance(Glance glance) {
    }

    public Notification getLastNotification() {
        return notifications.get(notifications.size() - 1);
    }
}
