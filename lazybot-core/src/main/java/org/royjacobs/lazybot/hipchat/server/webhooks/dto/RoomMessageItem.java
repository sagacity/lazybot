package org.royjacobs.lazybot.hipchat.server.webhooks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomMessageItem {
    public RoomMessageItemData message;
}
