package com.bol.lazybot.server.capability.dto;

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
