package org.royjacobs.lazybot.hipchat.server.capabilities.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Capabilities {
    private String name;
    private String description;
    private String key;
    private Links links;
    private CapabilitiesContent capabilities;
}
