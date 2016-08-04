package org.royjacobs.lazybot.api.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Notification {
    private String message;
    private Color color;
}
