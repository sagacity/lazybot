package com.bol.lazybot.hipchat.client;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateWebhookRequest {
    private String key;
    private String url;
    private String event;
}
