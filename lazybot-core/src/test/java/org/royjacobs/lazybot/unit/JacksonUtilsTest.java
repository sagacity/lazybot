package org.royjacobs.lazybot.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.royjacobs.lazybot.utils.JacksonUtils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JacksonUtilsTest {
    final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void roundTrip() {
        final JacksonUtilsTestClass obj = JacksonUtilsTestClass.builder().str("hello").build();
        final JacksonUtilsTestClass deserialized = JacksonUtils.deserialize(mapper, JacksonUtils.serialize(mapper, obj), JacksonUtilsTestClass.class);
        assertThat(deserialized, is(obj));
    }
}
