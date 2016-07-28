package org.royjacobs.lazybot.unit.utils;

import org.junit.Test;
import org.royjacobs.lazybot.utils.JacksonUtils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JacksonUtilsTest {
    @Test
    public void roundTrip() {
        final JacksonUtilsTestClass obj = JacksonUtilsTestClass.builder().str("hello").build();
        final JacksonUtilsTestClass deserialized = JacksonUtils.deserialize(JacksonUtils.serialize(obj), JacksonUtilsTestClass.class);
        assertThat(deserialized, is(obj));
    }
}
