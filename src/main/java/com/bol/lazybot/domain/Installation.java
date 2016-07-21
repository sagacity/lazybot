package com.bol.lazybot.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize(builder = Installation.InstallationBuilder.class)
@Value
@Builder
public class Installation {
    private String oauthId;
    private String oauthSecret;
    private String roomId;
    //private Room room;
    //private RoomRepository roomRepository;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class InstallationBuilder {
    }
}
