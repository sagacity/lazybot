package org.royjacobs.lazybot.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.royjacobs.lazybot.api.plugins.PluginRoomData;

@Data
@AllArgsConstructor
public class TestPluginRoomData implements PluginRoomData {
    private String data;
}
