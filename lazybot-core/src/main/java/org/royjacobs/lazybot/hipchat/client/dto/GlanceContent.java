package org.royjacobs.lazybot.hipchat.client.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GlanceContent {
    private final GlanceLabel label;
    private final GlanceStatus status;
}
