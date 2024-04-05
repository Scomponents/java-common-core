/*******************************************************************************
 * Copyright (c) 2008-2024 Intechcore GmbH - All Rights Reserved
 *
 * This file is part of SComponents project
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 *
 * Proprietary and confidential
 *
 * Written by Intechcore GmbH <info@intechcore.com>
 ******************************************************************************/

package com.intechcore.scomponents.common.core.event.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager implements IEventManager {

    private final Map<Class<?>, List<IListener<?>>> listenersMap = new HashMap<>();
    private final Map<Class<?>, List<?>> deferredEvents = new HashMap<>();

    @Override
    public <TEventData> boolean subscribe(Class<TEventData> eventType, IListener<TEventData> listener) {
        if (this.deferredEvents.containsKey(eventType)) {
            List<TEventData> deferredEvents = (List<TEventData>)this.deferredEvents.get(eventType);
            deferredEvents.forEach(listener::accept);
            deferredEvents.clear();
        }

        return this.getListeners(eventType).add(listener);
    }

    @Override
    public <TEventData> boolean subscribe(Class<TEventData> eventType, Runnable listener) {
        return this.subscribe(eventType, unused -> listener.run());
    }

    @Override
    public <TEventData> boolean unsubscribe(Class<TEventData> eventType, IListener<TEventData> listener) {
        return this.getListeners(eventType).remove(listener);
    }

    @Override
    public <TEventData> long notify(TEventData event) {
        return this.notifyListeners(this.getListeners(event.getClass()), event);
    }

    @Override
    public <TEventData> long notify(Class<TEventData> eventClass) {
        return this.notifyListeners(this.getListeners(eventClass), null);
    }

    private <TEventData> long notifyListeners(List<IListener<?>> listeners, TEventData event) {
        long[] count = new long[] { 0 };
        listeners.forEach(listener -> {
            ((IListener<TEventData>)listener).accept(event);
            count[0]++;
        });

        return count[0];
    }

    @Override
    public <TEventData> long notifyAnyway(TEventData event) {
        Class<?> eventClass = event.getClass();
        return this.notifyAnyway(eventClass, event);
    }

    private <TEventData> long notifyAnyway(Class<?> eventClass, TEventData event) {

        List<IListener<?>> listeners = this.getListeners(eventClass);

        if (listeners.isEmpty()) {
            List<TEventData> deferredEvents = (List<TEventData>)this.deferredEvents.computeIfAbsent(eventClass,
                    list -> new ArrayList<TEventData>());

            deferredEvents.add(event);
            return 0;
        }

        return this.notifyListeners(listeners, event);
    }

    @Override
    public <TEventData> long notifyAnyway(Class<TEventData> eventClass) {
        return this.notifyAnyway(eventClass, null);
    }

    private <TEventData> List<IListener<?>> getListeners(Class<TEventData> eventType) {
        return this.listenersMap.computeIfAbsent(eventType, listeners -> new ArrayList<>());
    }
}
