package com.bol.lazybot.hipchat.client;

public interface RoomApiFactory {
    RoomApi create(final RoomId roomId, final OAuthToken token);
}
