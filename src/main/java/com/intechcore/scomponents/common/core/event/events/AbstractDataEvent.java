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

package com.intechcore.scomponents.common.core.event.events;

import com.intechcore.scomponents.common.core.event.manager.IEventManager;

/**
 * Parent class to create an event for {@link IEventManager} with some data, for example,
 * to notify some parts of the code about particular data change.
 * <p>Used in SComponents.Toolbox library to exchange data between UI controls and commands</p>
 * @param <TData> the type of the data
 */
public abstract class AbstractDataEvent<TData> {
    protected final TData newValue;

    protected AbstractDataEvent(TData newValue) {
        this.newValue = newValue;
    }

    /**
     * Event data getter
     * @return the data
     */
    public TData getNewValue() {
        return this.newValue;
    }
}
