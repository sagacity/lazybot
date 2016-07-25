package org.royjacobs.lazybot.bot;

import org.royjacobs.lazybot.api.plugins.PublicVariables;
import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VariableCombiner {
    private List<Subscription> subscriptions = new ArrayList<>();

    private PublicVariables current = new PublicVariables();
    private PublishSubject<PublicVariables> currentObservable = PublishSubject.create();

    public void register(Observable<PublicVariables> publicVariables) {
        subscriptions.add(publicVariables.subscribe(pv -> onVariablesChanged(pv.getVariables())));
    }

    private void onVariablesChanged(Map<String, String> newVars) {
        for (Map.Entry<String, String> newKv : newVars.entrySet()) {
            current.getVariables().put(newKv.getKey(), newKv.getValue());
        }

        currentObservable.onNext(current);
    }

    public void unregisterAll() {
        subscriptions.forEach(Subscription::unsubscribe);
    }

    public Observable<PublicVariables> getCurrentVariables() {
        return currentObservable;
    }
}
