package org.royjacobs.lazybot.hipchat.server.capabilities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CapabilitiesContent {
    private Installable installable;

    @JsonProperty("hipchatApiConsumer")
    private HipChatApiConsumer hipChatApiConsumer;

    @Singular("webhook")
    private List<WebHook> webhook;
}
