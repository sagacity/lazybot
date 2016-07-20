package com.bol.lazybot.client;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface HipChatOAuth {
    @RequestLine("POST /v2/oauth/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @Body("grant_type=client_credentials&scope={scope}")
    RequestTokenResponse requestToken(@Param("scope") final String scope);
}
