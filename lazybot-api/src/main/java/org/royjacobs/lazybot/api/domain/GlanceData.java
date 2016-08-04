package org.royjacobs.lazybot.api.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GlanceData {
    String label;
    String lozengeLabel;
    Lozenge lozenge;
}
