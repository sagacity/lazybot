package org.royjacobs.lazybot.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Icon {
    String url;

    @JsonProperty("url@2x")
    String url2x;
}
