package org.royjacobs.lazybot.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JacksonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String serialize(final T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize object of type: " + object.getClass(), e);
            throw Throwables.propagate(e);
        }
    }

    public static <T> T deserialize(final String json, final Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            log.error("Could not deserialize object of type: " + type, e);
            throw Throwables.propagate(e);
        }
    }
}
