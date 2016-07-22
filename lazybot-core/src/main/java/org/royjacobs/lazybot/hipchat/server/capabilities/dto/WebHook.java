package org.royjacobs.lazybot.hipchat.server.capabilities.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class WebHook {
    private String url;
    private String pattern;
    private String event;
    private String authentication;
    private String name;
}
