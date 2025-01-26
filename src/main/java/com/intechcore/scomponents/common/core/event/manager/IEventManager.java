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

/**
 * Represents a strongly typed event manager
 */
public interface IEventManager {

    /**
     * Adds a listener into the storage
     * @param eventType the type of the desired event
     * @param listener the consumer of desired event. The consumer will be called with an instance of eventType
     * @return {@code true} in the case of successful subscription
     * @param <TEventData> the type of desired event
     */
    <TEventData> boolean subscribe(Class<TEventData> eventType, IListener<TEventData> listener);

    /**
     * Removes particular, given listener from the storage
     * @param <TEventData> the type of the desired event
     * @param eventType the type of the event to unsubscribe from
     * @param listener given consumer to be removed
     * @return {@code true} in the case of successful removing
     */
    <TEventData> boolean unsubscribe(Class<TEventData> eventType, IListener<TEventData> listener);

    /**
     * Calls all existing listeners of the given type of event
     * @param event an instance of the desired event. This instance is used as a parameter for all listeners
     * @return the number of the called listeners
     * @param <TEventData> the type of the desired event
     */
    <TEventData> long notify(TEventData event);

    /**
     * Calls all listeners of the given type of event. If there are no listeners, calls are deferring and will make
     * in time of a subscription to the event
     * @param event an instance of the desired event. All listeners get this instance as parameter
     * @return the number of the called listeners
     * @param <TEventData> the type of the desired event
     */
    <TEventData> long notifyAnyway(TEventData event);


    /**
     * Adds a listener that doesn't accept any data to the storage (for the events without data)
     * @param eventType the type of the desired event
     * @param listener the consumer of desired event. The consumer will be called with an instance of eventType
     * @return {@code IListener<TEventData>} in the case of successful subscription, {@code null} otherwise
     * @param <TEventData> the type of the desired event
     */
    <TEventData> IListener<TEventData> subscribe(Class<TEventData> eventType, Runnable listener);

    /**
     * Removes particular, given {@code Runnable} listener from the storage
     * @param <TEventData> the type of the desired event
     * @param eventType the type of the event to unsubscribe from
     * @param listener given consumer to be removed
     * @return number of removed listeners with given runnable
     */
    <TEventData> int unsubscribe(Class<TEventData> eventType, Runnable listener);

    /**
     * Calls all existing listeners of the given type of event. The listeners get {@code null} as eventData
     * in order not to create empty instances of events
     * @param eventClass the type of the desired event. All listeners get {@code null} as parameter
     * @return the number of the called listeners
     * @param <TEventData> the type of the desired event
     */
    <TEventData> long notify(Class<TEventData> eventClass);

    /**
     * Calls all listeners of the given type of event. If there are no listeners, calls are deferring and will make
     * in time of a subscription to the event.
     * <p>The listeners get {@code null} as eventData in order not to create empty instances of events
     * @param eventClass the desired type of event. All listeners get {@code null} as parameter
     * @return the number of the called listeners
     * @param <TEventData> the type of the desired event
     */
    <TEventData> long notifyAnyway(Class<TEventData> eventClass);

    /**
     * Represents the base type for all event consumers
     * @param <TEventData> the concrete type of event
     */
    interface IListener<TEventData> {

        /**
         * It will be accepted when the corresponding event is called
         * @param data the event data, an instance of the class of the corresponding event
         */
        void accept(TEventData data);
    }
}
