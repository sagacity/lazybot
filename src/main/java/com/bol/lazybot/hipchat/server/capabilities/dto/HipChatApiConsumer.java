package com.bol.lazybot.hipchat.server.capabilities.dto;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class HipChatApiConsumer {
    @Singular
    private List<String> scopes;
}
