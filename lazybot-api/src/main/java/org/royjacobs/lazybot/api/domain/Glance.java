package org.royjacobs.lazybot.api.domain;

public interface Glance {
    void update(GlanceData data);
    String getKey();
}
