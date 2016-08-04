package org.royjacobs.lazybot.hipchat.client.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GlanceLabel {
    private final String type;
    private final String value;
}
