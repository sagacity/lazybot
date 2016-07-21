package com.bol.lazybot.client;

public interface RoomApiFactory {
    RoomApi create(final RoomId roomId, final OAuthToken token);
}
