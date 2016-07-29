package org.royjacobs.lazybot.api.plugins;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class PublicVariables {
    @Getter
    Map<String, String> variables = new HashMap<>();
}
