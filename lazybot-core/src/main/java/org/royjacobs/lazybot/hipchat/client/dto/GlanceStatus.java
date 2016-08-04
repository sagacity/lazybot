package org.royjacobs.lazybot.hipchat.client.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GlanceStatus {
    private final String type;
    private final GlanceLozenge value;
}
