package org.royjacobs.lazybot.hipchat.client.dto;

import lombok.Builder;
import lombok.Value;
import org.royjacobs.lazybot.api.domain.Icon;

@Value
@Builder
public class CreateGlanceRequest {
    private final String key;
    private final String queryUrl;
    private final Icon icon;
    private final GlanceName name;
}
