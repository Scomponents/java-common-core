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

package core.event.manager;

import com.intechcore.scomponents.common.core.event.manager.EventManager;
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
        TestEvent testEvent = new TestEvent1(EXPECTED1);
        String[] actual = new String[1];
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[0] = eventData.stringData;
        });

        long callsCount = this.target.notify(testEvent);

        Assertions.assertEquals(EXPECTED1, actual[0]);
        Assertions.assertEquals(1, callsCount);
    }

    @Test
    void SubscribeNotify_TwoSubscribers_ItMustSubscribeAndNotifyCorrectly() {
        TestEvent testEvent = new TestEvent1(EXPECTED1);
        String[] actual = new String[2];
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[0] = eventData.stringData;
        });
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[1] = eventData.stringData;
        });

        long callsCount = this.target.notify(testEvent);

        Assertions.assertEquals(EXPECTED1, actual[0]);
        Assertions.assertEquals(EXPECTED1, actual[1]);
        Assertions.assertEquals(2, callsCount);
    }

    @Test
    void SubscribeNotify_TwoObjectTypedSubscribers_ItMustSubscribeAndNotifyCorrectly() {
        Object testEvent1 = new TestEvent1(EXPECTED1);
        Object testEvent2 = new TestEvent2(EXPECTED2);
        String[] actual = new String[2];
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[0] = eventData.stringData;
        });
        this.target.subscribe(TestEvent2.class, eventData -> {
            actual[1] = eventData.stringData;
        });

        long callsCount1 = this.target.notify(testEvent1);
        long callsCount2 = this.target.notify(testEvent2);

        Assertions.assertEquals(EXPECTED1, actual[0]);
        Assertions.assertEquals(EXPECTED2, actual[1]);
        Assertions.assertEquals(1, callsCount1);
        Assertions.assertEquals(1, callsCount2);
    }

    @Test
    void SubscribeNotify_TwoSubscribersTwoNotifies_ResultDataMustBeFromTheLastNotify() {
        TestEvent1 testEvent1 = new TestEvent1(EXPECTED1);
        TestEvent1 testEvent2 = new TestEvent1(EXPECTED2);
        String[] actual = new String[2];
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[0] = eventData.stringData;
        });
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[1] = eventData.stringData;
        });

        long callsCount1 = this.target.notify(testEvent1);
        long callsCount2 = this.target.notify(testEvent2);

        Assertions.assertEquals(EXPECTED2, actual[0]);
        Assertions.assertEquals(EXPECTED2, actual[1]);
        Assertions.assertEquals(2, callsCount1);
        Assertions.assertEquals(2, callsCount2);
    }

    @Test
    void SubscribeNotify_TwoSubscribersOneNotify_ItMustNotCallSubscriberWithoutNotify() {
        TestEvent testEvent2 = new TestEvent2(EXPECTED2);
        String[] actual = new String[2];
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[0] = eventData.stringData;
        });
        this.target.subscribe(TestEvent2.class, eventData -> {
            actual[1] = eventData.stringData;
        });

        long callsCount2 = this.target.notify(testEvent2);

        Assertions.assertNull(actual[0]);
        Assertions.assertEquals(EXPECTED2, actual[1]);
        Assertions.assertEquals(1, callsCount2);
    }

    @Test
    void SubscribeNotify_TwoSubscribersTwoNotifiesTwoEvents_EverySubscriberMustCalledOnce() {
        TestEvent testEvent1 = new TestEvent1(EXPECTED1);
        TestEvent testEvent2 = new TestEvent2(EXPECTED2);
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
        TestEvent testEvent1 = new TestEvent1(EXPECTED1);
        TestEvent testEvent2 = new TestEvent2(EXPECTED2);
        String[] actual = new String[2];
        this.target.subscribe(TestEvent1.class, eventData -> {
            actual[0] = eventData.stringData;
        });
        this.target.subscribe(TestEvent2.class, eventData -> {
            actual[1] = eventData.stringData;
        });

        long callsCount1 = this.target.notify(testEvent1);
        long callsCount2 = this.target.notify(testEvent2);

        Assertions.assertEquals(EXPECTED1, actual[0]);
        Assertions.assertEquals(EXPECTED2, actual[1]);
        Assertions.assertEquals(1, callsCount1);
        Assertions.assertEquals(1, callsCount2);
    }

    @Test
    void Notify_WithoutSubscribe_ItMustCallWithoutException() {
        TestEvent testEvent = new TestEvent1(EXPECTED1);

        Assertions.assertDoesNotThrow(() -> this.target.notify(testEvent));
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
