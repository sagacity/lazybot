package org.royjacobs.lazybot.unit.utils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize(builder = JacksonUtilsTestClass.JacksonUtilsTestClassBuilder.class)
@Value
@Builder
public class JacksonUtilsTestClass {
    String str;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class JacksonUtilsTestClassBuilder {
    }
}
