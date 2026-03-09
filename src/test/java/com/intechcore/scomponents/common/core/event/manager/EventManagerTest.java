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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventManagerTest {

    private final String EXPECTED1 = "exp1";
    private final String EXPECTED2 = "exp2";

    private EventManager target;

    @BeforeEach
    void beforeEach() {
        this.target = new EventManager();
    }

    @Test
    void SubscribeNotify_DefaultOrder_ItMustSubscribeAndNotifyCorrectly() {
        TestEvent testEvent = new TestEvent1(this.EXPECTED1);
        String[] actual = new String[1];
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[0] = eventData.stringData;
        });

        long callsCount = this.target.notify(testEvent);

        Assertions.assertEquals(this.EXPECTED1, actual[0]);
        Assertions.assertEquals(1, callsCount);
    }

    @Test
    void SubscribeNotify_TwoSubscribers_ItMustSubscribeAndNotifyCorrectly() {
        TestEvent testEvent = new TestEvent1(this.EXPECTED1);
        String[] actual = new String[2];
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[0] = eventData.stringData;
        });
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[1] = eventData.stringData;
        });

        long callsCount = this.target.notify(testEvent);

        Assertions.assertEquals(this.EXPECTED1, actual[0]);
        Assertions.assertEquals(this.EXPECTED1, actual[1]);
        Assertions.assertEquals(2, callsCount);
    }

    @Test
    void SubscribeNotify_TwoObjectTypedSubscribers_ItMustSubscribeAndNotifyCorrectly() {
        Object testEvent1 = new TestEvent1(this.EXPECTED1);
        Object testEvent2 = new TestEvent2(this.EXPECTED2);
        String[] actual = new String[2];
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[0] = eventData.stringData;
        });
        this.target.subscribe(TestEvent2.class, eventData -> {
            actual[1] = eventData.stringData;
        });

        long callsCount1 = this.target.notify(testEvent1);
        long callsCount2 = this.target.notify(testEvent2);

        Assertions.assertEquals(this.EXPECTED1, actual[0]);
        Assertions.assertEquals(this.EXPECTED2, actual[1]);
        Assertions.assertEquals(1, callsCount1);
        Assertions.assertEquals(1, callsCount2);
    }

    @Test
    void SubscribeNotifyUnsubscribe_TwoObjectTypedSubscribers_ItMustSubscribeAndNotifyCorrectly() {
        Object testEvent1 = new TestEvent1(this.EXPECTED1);
        Object testEvent2 = new TestEvent2(this.EXPECTED2);
        String[] actual = new String[3];
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[0] = eventData.stringData;
        });
        this.target.subscribe(TestEvent2.class, eventData -> {
            actual[1] = eventData.stringData;
        });

        IEventManager.IListener<TestEvent2> listener2 = eventData -> {
            actual[2] = eventData.stringData;
        };
        this.target.subscribe(TestEvent2.class, listener2);
        this.target.unsubscribe(TestEvent2.class, listener2);

        long callsCount1 = this.target.notify(testEvent1);
        long callsCount2 = this.target.notify(testEvent2);

        Assertions.assertEquals(this.EXPECTED1, actual[0]);
        Assertions.assertEquals(this.EXPECTED2, actual[1]);
        Assertions.assertNull(actual[2]);
        Assertions.assertEquals(1, callsCount1);
        Assertions.assertEquals(1, callsCount2);
    }

    @Test
    void SubscribeNotify_TwoSubscribersTwoNotifies_ResultDataMustBeFromTheLastNotify() {
        TestEvent1 testEvent1 = new TestEvent1(this.EXPECTED1);
        TestEvent1 testEvent2 = new TestEvent1(this.EXPECTED2);
        String[] actual = new String[2];
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[0] = eventData.stringData;
        });
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[1] = eventData.stringData;
        });

        long callsCount1 = this.target.notify(testEvent1);
        long callsCount2 = this.target.notify(testEvent2);

        Assertions.assertEquals(this.EXPECTED2, actual[0]);
        Assertions.assertEquals(this.EXPECTED2, actual[1]);
        Assertions.assertEquals(2, callsCount1);
        Assertions.assertEquals(2, callsCount2);
    }

    @Test
    void SubscribeNotify_TwoSubscribersOneNotify_ItMustNotCallSubscriberWithoutNotify() {
        TestEvent testEvent2 = new TestEvent2(this.EXPECTED2);
        String[] actual = new String[2];
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[0] = eventData.stringData;
        });
        this.target.subscribe(TestEvent2.class, eventData -> {
            actual[1] = eventData.stringData;
        });

        long callsCount2 = this.target.notify(testEvent2);

        Assertions.assertNull(actual[0]);
        Assertions.assertEquals(this.EXPECTED2, actual[1]);
        Assertions.assertEquals(1, callsCount2);
    }

    @Test
    void SubscribeNotify_TwoSubscribersTwoNotifiesTwoEvents_EverySubscriberMustCalledOnce() {
        TestEvent testEvent1 = new TestEvent1(this.EXPECTED1);
        TestEvent testEvent2 = new TestEvent2(this.EXPECTED2);
        int[] actual = new int[] {0, 0};
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[0]++;
        });
        this.target.subscribe(TestEvent2.class, eventData -> {
            actual[1]++;
        });

        long callsCount1 = this.target.notify(testEvent1);
        long callsCount2 = this.target.notify(testEvent2);

        Assertions.assertEquals(1, actual[0]);
        Assertions.assertEquals(1, actual[1]);
        Assertions.assertEquals(1, callsCount1);
        Assertions.assertEquals(1, callsCount2);
    }

    @Test
    void SubscribeNotify_TwoSubscribersTwoNotifiesTwoEvents_ResultDataMustBeCorrect() {
        TestEvent testEvent1 = new TestEvent1(this.EXPECTED1);
        TestEvent testEvent2 = new TestEvent2(this.EXPECTED2);
        String[] actual = new String[2];
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[0] = eventData.stringData;
        });
        this.target.subscribe(TestEvent2.class, eventData -> {
            actual[1] = eventData.stringData;
        });

        long callsCount1 = this.target.notify(testEvent1);
        long callsCount2 = this.target.notify(testEvent2);

        Assertions.assertEquals(this.EXPECTED1, actual[0]);
        Assertions.assertEquals(this.EXPECTED2, actual[1]);
        Assertions.assertEquals(1, callsCount1);
        Assertions.assertEquals(1, callsCount2);
    }

    @Test
    void Notify_WithoutSubscribe_ItMustCallWithoutExceptionAndNoFalseNotifies() {
        TestEvent testEvent = new TestEvent1(this.EXPECTED1);
        long[] counter = new long[] { 0 };
        String[] actual = new String[] { "", "" };
        this.target.subscribe(TestEvent2.class, eventData -> {
            actual[0] += eventData.stringData;
        });

        Assertions.assertDoesNotThrow(() -> {
            counter[0] += this.target.notify(testEvent);
        });

        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[1] += eventData.stringData;
        });

        Assertions.assertEquals(0, counter[0]);
        Assertions.assertEquals("", actual[0]);
        Assertions.assertEquals("", actual[1]);
    }

    @Test
    void SubscribeNotifyRunners_TwoSubscribersTwoNotifiesTwoEvents_ResultDataMustBeCorrect() {
        class Event1 {}
        class Event2 {}

        final String EXP1 = Event1.class.getSimpleName();
        final String EXP2 = Event2.class.getSimpleName();

        String[] actual = new String[] { "", "" };
        this.target.subscribe(Event1.class, () -> {
            actual[0] += EXP1;
        });
        Runnable event2 = () -> {
            actual[1] += EXP2;
        };
        this.target.subscribe(Event2.class, event2);
        this.target.subscribe(Event2.class, event2);

        long callsCount1 = this.target.notify(Event1.class);
        long callsCount2 = this.target.notify(Event2.class);

        Assertions.assertEquals(EXP1, actual[0]);
        Assertions.assertEquals(EXP2 + EXP2, actual[1]);
        Assertions.assertEquals(1, callsCount1);
        Assertions.assertEquals(2, callsCount2);
    }

    @Test
    void SubscribeUnsubscribeNotifyRunners_TwoSubscribersTwoNotifiesTwoEvents_ItMustRunOnlySubscribedEvents() {
        class Event1 {}
        class Event2 {}

        final String EXP1 = Event1.class.getSimpleName();
        final String EXP2 = Event2.class.getSimpleName();

        String[] actual = new String[] { "", "" };
        this.target.subscribe(Event1.class, () -> {
            actual[0] += EXP1;
        });

        Runnable event2 = () -> {
            actual[1] += EXP2;
        };
        this.target.subscribe(Event2.class, event2);
        this.target.subscribe(Event2.class, event2);
        this.target.unsubscribe(Event2.class, event2);

        long callsCount1 = this.target.notify(Event1.class);
        long callsCount2 = this.target.notify(Event2.class);

        Assertions.assertEquals(EXP1, actual[0]);
        Assertions.assertEquals("", actual[1]);
        Assertions.assertEquals(1, callsCount1);
        Assertions.assertEquals(0, callsCount2);
    }

    @Test
    void SubscribeUnsubscribeNotifyRunners_OneEventTwoSubscribers_ItMustCorrectUnsubscribeAndNotifyTheRest() {
        class Event {}

        final String EXP = Event.class.getSimpleName();

        String[] actual = new String[] { "", "", "" };
        this.target.subscribe(Event.class, () -> {
            actual[0] += EXP;
        });
        this.target.subscribe(Event.class, () -> {
            actual[1] += EXP;
        });
        Runnable event = () -> {
            actual[2] += EXP;
        };
        this.target.subscribe(Event.class, event);
        this.target.subscribe(Event.class, event);
        long unsubscribed = this.target.unsubscribe(Event.class, event);

        long callsCount = this.target.notify(Event.class);

        Assertions.assertEquals(EXP, actual[0]);
        Assertions.assertEquals(EXP, actual[1]);
        Assertions.assertEquals("", actual[2]);
        Assertions.assertEquals(2, callsCount);
        Assertions.assertEquals(2, unsubscribed);
    }

    @Test
    void UnsubscribeRunner_SetOfRunners_ItMustCorrectUnsubscribeAndRunRestEvents() {
        class Event {}

        final String EXP = Event.class.getSimpleName();

        String[] actual = new String[] { "", "", "" };
        this.target.subscribe(Event.class, () -> {
            actual[0] += EXP;
        });
        this.target.subscribe(Event.class, () -> {
            actual[1] += EXP;
        });
        IEventManager.IListener<Event> unsubsribeListener = this.target.subscribe(Event.class, () -> {
            actual[2] += EXP;
        });
        this.target.unsubscribe(Event.class, unsubsribeListener);

        long callsCount1 = this.target.notify(Event.class);

        Assertions.assertEquals(EXP, actual[0]);
        Assertions.assertEquals(EXP, actual[1]);
        Assertions.assertEquals("", actual[2]);
        Assertions.assertEquals(2, callsCount1);
    }

    public static abstract class TestEvent {
        public final String stringData;

        public TestEvent(String stringData) {
            this.stringData = stringData;
        }
    }

    private static class TestEvent1 extends TestEvent {
        public TestEvent1(String stringData) {
            super(stringData);
        }
    }

    private static class TestEvent2 extends TestEvent {
        public TestEvent2(String stringData) {
            super(stringData);
        }
    }
}
