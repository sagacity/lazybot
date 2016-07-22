package org.royjacobs.lazybot.hipchat.client.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class OAuthToken {
    String value;
}