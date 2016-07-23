package org.royjacobs.lazybot.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomMessageItem {
    public RoomMessageItemData message;
}
