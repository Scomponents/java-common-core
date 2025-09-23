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
 * A strongly-typed event management system for publishing and subscribing to events
 *
 * <p>This interface defines a type-safe event bus that allows components to communicate
 * through events while maintaining type safety through generics. It supports both
 * events with data payloads and parameterless events.
 *
 * <p><b>Key features:</b>
 * <ul>
 *   <li>Type-safe event subscription and publication</li>
 *   <li>Support for events with data payloads and parameterless events</li>
 *   <li>Deferred notification for events without current subscribers</li>
 *   <li>Flexible listener registration with both {@link IListener} and {@link Runnable}</li>
 * </ul>
 *
 * <p><b>Example usage:</b>
 * <pre>{@code
 * IEventManager eventManager = ...;
 *
 * // Subscribe to events with data
 * eventManager.subscribe(UserLoginEvent.class, (event) -> {
 *     System.out.println("User logged in: " + event.getUsername());
 * });
 *
 * // Subscribe to parameterless events
 * eventManager.subscribe(ApplicationShutdownEvent.class, () -> {
 *     System.out.println("Application is shutting down");
 * });
 *
 * // Publish events
 * eventManager.notify(new UserLoginEvent("john_doe"));
 * eventManager.notify(ApplicationShutdownEvent.class);
 * }</pre>
 */
public interface IEventManager {

    /**
     * Subscribes a listener to receive events of the specified type
     *
     * <p>When an event of type {@code TEventData} is published, the provided listener
     * will be invoked with the event instance as a parameter
     *
     * @param <TEventData> the type of event to subscribe to
     * @param eventType the class object representing the type of event to subscribe to
     * @param listener the listener that will be invoked when events of the specified type are published
     * @return {@code true} if the listener was successfully subscribed, {@code false} otherwise
     * @throws NullPointerException if either {@code eventType} or {@code listener} is null
     */
    <TEventData> boolean subscribe(Class<TEventData> eventType, IListener<TEventData> listener);


    /**
     * Unsubscribes a specific listener from events of the specified type
     *
     * @param <TEventData> the type of event to unsubscribe from
     * @param eventType the class object representing the type of event to unsubscribe from
     * @param listener the listener to be removed from the subscription list
     * @return {@code true} if the listener was successfully unsubscribed, {@code false} otherwise
     * @throws NullPointerException if either {@code eventType} or {@code listener} is null
     */
    <TEventData> boolean unsubscribe(Class<TEventData> eventType, IListener<TEventData> listener);


    /**
     * Publishes an event to all currently subscribed listeners of that event type
     *
     * <p>Only listeners that are currently subscribed will receive the event.
     * If there are no subscribers for this event type, the event will be ignored
     *
     * @param <TEventData> the type of the event being published
     * @param event the event instance to publish to all subscribed listeners
     * @return the number of listeners that were notified
     * @throws NullPointerException if {@code event} is null
     */
    <TEventData> long notify(TEventData event);


    /**
     * Publishes an event to all current and future subscribers.
     *
     * <p>If there are currently no subscribers for this event type, the notification
     * will be deferred and delivered to any subscribers that register later.
     *
     * @param <TEventData> the type of the event being published
     * @param event the event instance to publish to current and future subscribers
     * @return the number of listeners that were immediately notified (current subscribers)
     * @throws NullPointerException if {@code event} is null
     */
    <TEventData> long notifyAnyway(TEventData event);


    /**
     * Subscribes a parameterless listener to events of the specified type
     *
     * <p>This method allows using a {@link Runnable} for events that don't carry data.
     * When the event is published, the runnable will be invoked without any parameters
     *
     * @param <TEventData> the type of event to subscribe to
     * @param eventType the class object representing the type of event to subscribe to
     * @param listener the runnable that will be invoked when events of the specified type are published
     * @return the wrapper listener that was created, or {@code null} if subscription failed
     * @throws NullPointerException if either {@code eventType} or {@code listener} is null
     */
    <TEventData> IListener<TEventData> subscribe(Class<TEventData> eventType, Runnable listener);


    /**
     * Unsubscribes all runnable listeners of the specified type
     *
     * @param <TEventData> the type of event to unsubscribe from
     * @param eventType the class object representing the type of event to unsubscribe from
     * @param listener the runnable to be removed from subscriptions
     * @return the number of listeners that were successfully removed
     * @throws NullPointerException if either {@code eventType} or {@code listener} is null
     */
    <TEventData> int unsubscribe(Class<TEventData> eventType, Runnable listener);


    /**
     * Publishes a parameterless event to all currently subscribed listeners
     *
     * <p>This method is used for events that don't carry data. Listeners will receive
     * a {@code null} value instead of an event instance.
     *
     * @param <TEventData> the type of the event being published
     * @param eventClass the class object representing the type of event to publish
     * @return the number of listeners that were notified
     * @throws NullPointerException if {@code eventClass} is null
     */
    <TEventData> long notify(Class<TEventData> eventClass);


    /**
     * Publishes a parameterless event to all current and future subscribers
     *
     * <p>If there are currently no subscribers, the notification will be deferred
     * and delivered to any subscribers that register later. Listeners will receive
     * a {@code null} value instead of an event instance.
     *
     * @param <TEventData> the type of the event being published
     * @param eventClass the class object representing the type of event to publish
     * @return the number of listeners that were immediately notified
     * @throws NullPointerException if {@code eventClass} is null
     */
    <TEventData> long notifyAnyway(Class<TEventData> eventClass);


    /**
     * A listener interface for receiving events with data payloads
     *
     * <p>Implementations of this interface can be registered with the event manager
     * to receive events of a specific type
     *
     * @param <TEventData> the type of event data this listener can handle
     */
    interface IListener<TEventData> {


        /**
         * Called when an event of the subscribed type is published
         *
         * @param data the event data instance, containing information about the event
         */
        void accept(TEventData data);
    }
}
