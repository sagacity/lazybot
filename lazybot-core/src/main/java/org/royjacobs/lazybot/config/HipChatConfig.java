package org.royjacobs.lazybot.config;

import lombok.Data;

import java.util.Set;

@Data
public class HipChatConfig {
    private Set<String> scopes;
    private String hipChatApiUrl = "https://api.hipchat.com";

    public String getRequestTokenUrl() {
        return hipChatApiUrl + "/v2/oauth/token";
    }

    public String getRoomUrl(String roomId) {
        return hipChatApiUrl + "/v2/room/" + roomId;
    }
}
