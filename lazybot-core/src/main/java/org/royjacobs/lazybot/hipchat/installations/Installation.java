package org.royjacobs.lazybot.hipchat.installations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@JsonDeserialize(builder = Installation.InstallationBuilder.class)
@Value
@Builder
@EqualsAndHashCode
public class Installation {
    private String oauthId;
    private String oauthSecret;
    private String roomId;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class InstallationBuilder {
    }
}
