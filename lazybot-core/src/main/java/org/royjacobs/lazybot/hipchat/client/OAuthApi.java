package org.royjacobs.lazybot.hipchat.client;

import org.royjacobs.lazybot.hipchat.client.dto.RequestTokenResponse;

import java.io.IOException;

public interface OAuthApi {
    RequestTokenResponse requestToken(final String oauthId, final String oauthSecret) throws IOException;
}
