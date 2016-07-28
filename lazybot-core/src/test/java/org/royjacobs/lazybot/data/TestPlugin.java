package org.royjacobs.lazybot.data;

import lombok.Getter;
import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.plugins.*;
import rx.subjects.PublishSubject;

public class TestPlugin implements Plugin {
    private final String key;
    private final PublishSubject<PublicVariables> publicVariables = PublishSubject.create();
    private final PublicVariables vars = new PublicVariables();

    @Getter
    private PluginContext context;

    @Getter
    private boolean unregistered;

    public TestPlugin(String key) {
        this.key = key;
    }

    @Override
    public PluginDescriptor getDescriptor() {
        return PluginDescriptor.builder()
                .key(key)
                .publicVariables(publicVariables)
                .configDataClass(TestPluginConfigData.class)
                .roomDataClass(TestPluginRoomData.class)
                .globalDataClass(TestPluginGlobalData.class)
                .build();
    }

    @Override
    public void onStart(PluginContext context) {
        this.context = context;
    }

    @Override
    public void onStop(boolean unregistered) {
        this.unregistered = unregistered;
        this.context = null;
    }

    @Override
    public PluginMessageHandlingResult onCommand(Command command) {
        return PluginMessageHandlingResult.SUCCESS;
    }

    public void setVariable(String key, String value) {
        if (value.equals("<removed>")) vars.getVariables().remove(key);
        else vars.getVariables().put(key, value);
        this.publicVariables.onNext(vars);
    }

    public boolean isStopped() {
        return context == null;
    }
}
