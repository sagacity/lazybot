package org.royjacobs.lazybot.hipchat.client.dto;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UpdateGlanceRequest {
    @Singular("glance")
    private final List<GlanceUpdate> glance;
}
