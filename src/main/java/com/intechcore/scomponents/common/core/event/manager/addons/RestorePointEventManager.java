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
 * A decorator for {@link IEventManager} that provides the ability to track and remove
 * subscribers added through this specific instance
 *
 * <p>This class wraps an existing {@code IEventManager} and maintains a record of all
 * subscribers added via its methods. This allows for convenient cleanup of subscriptions
 * without affecting subscribers that were registered directly with the underlying event manager
 *
 * <p><b>Key features:</b>
 * <ul>
 *   <li>Tracks all subscribers added through this decorator instance</li>
 <li>Provides bulk removal of tracked subscribers via {@link #removeStoredSubscribers()}</li>
 *   <li>Maintains isolation from subscribers registered directly with the underlying event manager</li>
 *   <li>Useful for temporary event handling scenarios that require clean cleanup</li>
 * </ul>
 *
 * <p><b>Typical usage pattern:</b>
 * <pre>{@code
 * IEventManager mainEventManager = ...;
 * RestorePointEventManager tempManager = new RestorePointEventManager(mainEventManager);
 *
 * // Subscribe to events through the restore point manager
 * tempManager.subscribe(MyEvent.class, this::handleEvent);
 *
 * // ... perform operations that use these subscriptions ...
 *
 * // Clean up all subscriptions made through this restore point
 * tempManager.removeStoredSubscribers();
 * }</pre>
 *
 * <p>This is particularly useful in scenarios where a component needs to temporarily
 * listen to events and then clean up all its subscriptions without affecting other
 * components that may have registered listeners with the underlying event manager.
 *
 * @see IEventManager
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
     * Removes all subscribers that were added through this restore point instance
     *
     * <p>This method unsubscribes all listeners that were registered via this decorator
     * from the underlying event manager. Subscribers that were registered directly
     * with the underlying event manager are not affected
     *
     * <p>After calling this method, the internal tracking list is cleared
     *
     * @return the total number of subscribers that were removed
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
