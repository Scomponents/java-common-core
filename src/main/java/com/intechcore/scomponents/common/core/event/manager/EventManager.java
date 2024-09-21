/*
 * Copyright (c) 2024-present, Intechcore GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
