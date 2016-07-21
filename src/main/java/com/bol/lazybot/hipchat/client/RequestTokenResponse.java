package com.bol.lazybot.hipchat.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
public class RequestTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    private String scope;

    @JsonProperty("expires_in")
    private long expiresIn;

    @JsonProperty("group_name")
    private String groupName;

    @JsonProperty("group_id")
    private String groupId;
}
