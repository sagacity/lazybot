package org.royjacobs.lazybot.api.hipchat;

import org.royjacobs.lazybot.api.domain.*;

public interface RoomApi {
    void sendNotification(final Notification notification);
    void setTopic(final String topic);

    Glance registerGlance(final String glanceKey, final Icon icon, final GlanceData initialData);
    void unregisterGlance(final Glance glance);
}
