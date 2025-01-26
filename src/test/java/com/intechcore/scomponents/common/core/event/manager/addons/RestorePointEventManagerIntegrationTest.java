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

import com.intechcore.scomponents.common.core.event.manager.EventManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

class RestorePointEventManagerIntegrationTest {
    private EventManager internalManager;
    private RestorePointEventManager target;

    @BeforeEach
    void beforeEach() {
        this.internalManager = new EventManager();
        this.target = new RestorePointEventManager(this.internalManager);
    }

    @Test
    void AllInOne_CorrectInput_ItMustNotifyAllClearSubscribedToTargetAndLeaveToInternal() {
        class Event1 { }
        class Event2 { }
        class EventRunner { }

        Map<Class<?>, AtomicInteger> notifiesCounters = new HashMap<>();
        Consumer<Object> notifyCounter = eventClass -> {
            notifiesCounters.computeIfAbsent(eventClass.getClass(), e -> new AtomicInteger(0))
                    .incrementAndGet();
        };

        Runnable setup = () -> {
            this.internalManager.subscribe(Event1.class, notifyCounter::accept);
            this.internalManager.subscribe(Event1.class, notifyCounter::accept);
            this.internalManager.subscribe(Event2.class, notifyCounter::accept);
            this.internalManager.subscribe(Event2.class, notifyCounter::accept);
            this.internalManager.subscribe(EventRunner.class, () -> {
                notifyCounter.accept(new EventRunner());
            });
            this.internalManager.subscribe(EventRunner.class, () -> {
                notifyCounter.accept(new EventRunner());
            });
            this.target.subscribe(Event1.class, notifyCounter::accept);
            this.target.subscribe(Event1.class, notifyCounter::accept);
            this.target.subscribe(Event2.class, notifyCounter::accept);
            this.target.subscribe(Event2.class, notifyCounter::accept);
            this.target.subscribe(EventRunner.class, () -> {
                notifyCounter.accept(new EventRunner());
            });
            this.target.subscribe(EventRunner.class, () -> {
                notifyCounter.accept(new EventRunner());
            });
        };

        Consumer<Integer> notifyActAndCheck = eachEventExpectedInvocations -> {
            notifiesCounters.clear();

            this.internalManager.notify(new Event1());
            this.internalManager.notify(new Event2());
            this.internalManager.notify(EventRunner.class);
            this.target.notify(new Event1());
            this.target.notify(new Event2());
            this.target.notify(EventRunner.class);

            Assertions.assertEquals(eachEventExpectedInvocations, notifiesCounters.get(Event1.class).get());
            Assertions.assertEquals(eachEventExpectedInvocations, notifiesCounters.get(Event2.class).get());
            Assertions.assertEquals(eachEventExpectedInvocations, notifiesCounters.get(EventRunner.class).get());
        };

        Runnable removeSubscribersActAndCheck = () -> {
            int removedSubscribers = this.target.removeStoredSubscribers();
            Assertions.assertEquals(6, removedSubscribers);
        };

        setup.run();

        notifyActAndCheck.accept(8);
        removeSubscribersActAndCheck.run();
        notifyActAndCheck.accept(4);
    }
}
