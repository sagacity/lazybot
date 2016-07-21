package com.bol.lazybot.hipchat.server.webhooks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private long id;
    @JsonProperty("mention_name")
    private String mentionName;
    private String name;
}
