/*
 * Copyright (c) 2025-present, Intechcore GmbH
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

package com.intechcore.scomponents.common.core.event.manager.addons;

import com.intechcore.scomponents.common.core.event.manager.IEventManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Decorator of the {@code IEventManager} to have possibility to {@link #removeStoredSubscribers()}, other hand, from
 * the moment of creation, user will have possibility to remove all the new subscribers added with this instance
 * to convenient clean of the internal target {@link IEventManager}
 */
public class RestorePointEventManager implements IEventManager {
    private final IEventManager target;
    private final Map<Class<?>, List<IListener<?>>> listenersToRemove = new HashMap<>();

    public RestorePointEventManager(IEventManager target) {
        this.target = target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TEventData> boolean subscribe(Class<TEventData> eventType, IListener<TEventData> listener) {
        boolean listenerWasAdded = this.target.subscribe(eventType, listener);
        if (listenerWasAdded) {
            this.addListenerToRemove(eventType, listener);
        }
        return listenerWasAdded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TEventData> IListener<TEventData> subscribe(Class<TEventData> eventType, Runnable runnable) {
        IListener<TEventData> result = this.target.subscribe(eventType, runnable);
        if (result != null) {
            this.addListenerToRemove(eventType, result);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TEventData> int unsubscribe(Class<TEventData> eventType, Runnable listener) {
        return this.target.unsubscribe(eventType, listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TEventData> boolean unsubscribe(Class<TEventData> eventType, IListener<TEventData> listener) {
        return this.target.unsubscribe(eventType, listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TEventData> long notify(TEventData event) {
        return this.target.notify(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TEventData> long notify(Class<TEventData> eventClass) {
        return this.target.notify(eventClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TEventData> long notifyAnyway(TEventData event) {
        return this.target.notifyAnyway(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TEventData> long notifyAnyway(Class<TEventData> eventClass) {
        return this.target.notifyAnyway(eventClass);
    }

    /**
     *
     * @return number of removed listeners
     */
    public int removeStoredSubscribers() {
        int[] counter = new int[] { 0 };
        this.listenersToRemove.forEach((eventType, listeners) -> {
            listeners.forEach(listener -> {
                if (this.removeListener(eventType, listener)) {
                    counter[0]++;
                }
            });
        });

        this.listenersToRemove.clear();

        return counter[0];
    }

    private <TEvent> void addListenerToRemove(Class<?> eventClass, IListener<?> listener) {
        this.listenersToRemove.computeIfAbsent(eventClass, k -> new ArrayList<>()).add(listener);
    }

    private <TEvent> boolean removeListener(Class<?> eventClass, IListener<?> listener) {
        boolean success = this.target.unsubscribe((Class<TEvent>)eventClass, (IListener<TEvent>)listener);
        return success;
    }
}
