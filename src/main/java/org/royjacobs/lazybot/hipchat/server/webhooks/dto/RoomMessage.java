package org.royjacobs.lazybot.hipchat.server.webhooks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomMessage {
    private RoomMessageItem item;

    @JsonProperty("oauth_client_id")
    private String oauthId;
}
