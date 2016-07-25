package org.royjacobs.lazybot.api.plugins;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class PublicVariables {
    @Getter
    Map<String, String> variables;

    public PublicVariables() {
        variables = new HashMap<>();
    }
}
