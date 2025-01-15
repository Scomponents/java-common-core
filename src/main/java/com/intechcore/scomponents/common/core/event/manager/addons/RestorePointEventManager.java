/*******************************************************************************
 *  Copyright (C) 2008-2024 Intechcore GmbH - All Rights Reserved
 *
 *  This file is part of SComponents project
 *
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *
 *  Proprietary and confidential
 *
 *  Written by Intechcore GmbH <info@intechcore.com>
 ******************************************************************************/

package com.intechcore.scomponents.common.core.event.manager.addons;

import com.intechcore.scomponents.common.core.event.manager.IEventManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestorePointEventManager implements IEventManager {
    private final IEventManager target;
    private final Map<Class<?>, List<IListener<?>>> listenersToRemove = new HashMap<>();

    public RestorePointEventManager(IEventManager target) {
        this.target = target;
    }

    @Override
    public <TEventData> boolean subscribe(Class<TEventData> eventType, IListener<TEventData> listener) {
        boolean listenerWasAdded = this.target.subscribe(eventType, listener);
        if (listenerWasAdded) {
            this.addListenerToRemove(eventType, listener);
        }
        return listenerWasAdded;
    }

    @Override
    public <TEventData> IListener<TEventData> subscribe(Class<TEventData> eventType, Runnable runnable) {
        IListener<TEventData> result = this.target.subscribe(eventType, runnable);
        if (result != null) {
            this.addListenerToRemove(eventType, result);
        }

        return result;
    }

    @Override
    public <TEventData> int unsubscribe(Class<TEventData> eventType, Runnable listener) {
        return this.target.unsubscribe(eventType, listener);
    }

    @Override
    public <TEventData> boolean unsubscribe(Class<TEventData> eventType, IListener<TEventData> listener) {
        return this.target.unsubscribe(eventType, listener);
    }

    @Override
    public <TEventData> long notify(TEventData event) {
        return this.target.notify(event);
    }

    @Override
    public <TEventData> long notify(Class<TEventData> eventClass) {
        return this.target.notify(eventClass);
    }

    @Override
    public <TEventData> long notifyAnyway(TEventData event) {
        return this.target.notifyAnyway(event);
    }

    @Override
    public <TEventData> long notifyAnyway(Class<TEventData> eventClass) {
        return this.target.notifyAnyway(eventClass);
    }

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
