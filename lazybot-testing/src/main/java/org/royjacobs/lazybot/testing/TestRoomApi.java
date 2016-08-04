package org.royjacobs.lazybot.testing;

import lombok.Getter;
import org.royjacobs.lazybot.api.domain.*;
import org.royjacobs.lazybot.api.hipchat.RoomApi;

import java.util.ArrayList;
import java.util.List;

public class TestRoomApi implements RoomApi {
    @Getter
    private final List<String> notifications = new ArrayList<>();

    @Getter
    private final List<String> topics = new ArrayList<>();

    @Override
    public void sendMessage(String message, Color color) {
        notifications.add(message);
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

    public String getLastNotification() {
        return notifications.get(notifications.size() - 1);
    }

    public String getLastTopic() {
        return topics.get(topics.size() - 1);
    }
}
