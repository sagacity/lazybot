package org.royjacobs.lazybot.hipchat.client;

import org.royjacobs.lazybot.hipchat.installations.Installation;

public interface RoomApiFactory {
    RoomApi create(final Installation installation);
}
