package org.royjacobs.lazybot.hipchat.client.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GlanceLozenge {
    private final String type;
    private final String label;
}
