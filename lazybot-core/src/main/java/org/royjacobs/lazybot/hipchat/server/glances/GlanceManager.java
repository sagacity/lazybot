package org.royjacobs.lazybot.hipchat.server.glances;

import org.royjacobs.lazybot.api.domain.GlanceData;
import org.royjacobs.lazybot.hipchat.client.dto.GlanceContent;
import org.royjacobs.lazybot.hipchat.client.dto.GlanceLabel;
import org.royjacobs.lazybot.hipchat.client.dto.GlanceLozenge;
import org.royjacobs.lazybot.hipchat.client.dto.GlanceStatus;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class GlanceManager {
    private final Map<String, GlanceData> glances = new HashMap<>();

    public void registerGlance(String oauthId, String key, GlanceData data) {
        glances.put(makeKey(oauthId, key), data);
    }

    public void unregisterGlance(String oauthId, String key) {
        glances.remove(makeKey(oauthId, key));
    }

    public Optional<GlanceData> get(String oauthId, String key) {
        return Optional.ofNullable(glances.get(makeKey(oauthId, key)));
    }

    public GlanceContent toContent(GlanceData glanceData) {
        return GlanceContent.builder()
                .label(GlanceLabel.builder()
                        .type("html")
                        .value(glanceData.getLabel())
                        .build())
                .status(GlanceStatus.builder()
                        .type("lozenge")
                        .value(GlanceLozenge.builder()
                                .type(glanceData.getLozenge().name().toLowerCase())
                                .label(glanceData.getLozengeLabel())
                                .build())
                        .build())
                .build();
    }

    private String makeKey(String oauthId, String key) {
        return oauthId + "#" + key;
    }
}
