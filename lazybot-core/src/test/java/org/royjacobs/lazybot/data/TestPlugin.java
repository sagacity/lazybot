package org.royjacobs.lazybot.data;

import org.royjacobs.lazybot.api.domain.Command;
import org.royjacobs.lazybot.api.plugins.*;
import rx.subjects.PublishSubject;

public class TestPlugin implements Plugin {
    private final String key;
    private final PublishSubject<PublicVariables> publicVariables = PublishSubject.create();
    private final PublicVariables vars = new PublicVariables();

    public TestPlugin(String key) {
        this.key = key;
    }

    @Override
    public PluginDescriptor getDescriptor() {
        return PluginDescriptor.builder()
                .key(key)
                .publicVariables(publicVariables)
                .build();
    }

    @Override
    public void onStart(PluginContext context) {

    }

    @Override
    public void onStop(boolean removed) {

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
}
