package org.royjacobs.lazybot.hipchat.client;

import org.royjacobs.lazybot.api.hipchat.RoomApi;
import org.royjacobs.lazybot.api.domain.Installation;

public interface RoomApiFactory {
    RoomApi create(final Installation installation);
}
