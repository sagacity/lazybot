package com.bol.lazybot.hipchat.client;

import com.bol.lazybot.hipchat.installations.Installation;

public interface RoomApiFactory {
    RoomApi create(final Installation installation);
}
