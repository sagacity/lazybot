package org.royjacobs.lazybot.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.royjacobs.lazybot.api.plugins.PluginConfigData;

@Data
@AllArgsConstructor
public class TestPluginConfigData implements PluginConfigData {
    String data;
}
