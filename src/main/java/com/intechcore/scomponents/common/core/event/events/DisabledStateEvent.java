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
 * Parent class to create an event for {@link IEventManager} to notify some UI control or command
 * to change its state to disable.
 * <p>Used in SComponents.Toolbox library to disable/enable UI controls depends application state</p>
 */
public abstract class DisabledStateEvent {
    private final Boolean disabled;

    protected DisabledStateEvent(Boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * New state getter
     * @return the disabled state value
     */
    public Boolean getDisabled() {
        return this.disabled;
    }
}
