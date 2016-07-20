package com.bol.lazybot.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize(builder = RequestTokenResponse.RequestTokenResponseBuilder.class)
@Value
@Builder
public class RequestTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    private String scope;

    @JsonProperty("expires_in")
    private long expiresIn;
}
