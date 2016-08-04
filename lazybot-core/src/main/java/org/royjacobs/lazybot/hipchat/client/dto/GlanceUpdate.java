package org.royjacobs.lazybot.hipchat.client.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GlanceUpdate {
    private final GlanceContent content;
    private final String key;
}
