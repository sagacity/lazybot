package com.bol.lazybot.client;

import java.io.IOException;

public interface OAuthApi {
    RequestTokenResponse requestToken(final String oauthId, final String oauthSecret) throws IOException;
}
