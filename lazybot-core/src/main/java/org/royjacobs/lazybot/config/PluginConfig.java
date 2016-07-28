package org.royjacobs.lazybot.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class PluginConfig {
    private Map<String, Object> parameters = new HashMap<>();
}
