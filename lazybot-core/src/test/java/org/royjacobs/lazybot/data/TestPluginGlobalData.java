package org.royjacobs.lazybot.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.royjacobs.lazybot.api.plugins.PluginGlobalData;

@Data
@AllArgsConstructor
public class TestPluginGlobalData implements PluginGlobalData {
    private String data;
}
