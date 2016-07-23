package org.royjacobs.lazybot.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomMessageItemData {
    private String date;
    private User from;
    private String id;
    private String message;
}
