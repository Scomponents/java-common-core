/*******************************************************************************
 * Copyright (c) 2008-2024 Intechcore GmbH - All Rights Reserved
 *
 * This file is part of SComponents project
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 *
 * Proprietary and confidential
 *
 * Written by Intechcore GmbH <info@intechcore.com>
 ******************************************************************************/

package com.intechcore.scomponents.common.core.event.events;

/**
 * The main class of all API-supported events.
 * <p>There are two kinds of API-events:
 * <p>1. The ones that have '...ChangedEvent' at the end of the name. They inform about the changes of the
 * internal states
 * <p>2. The ones that have '...Request' at the end of the name. They are responsible for the UI control performance of the corresponding operation
 * @param <TData> the type of concrete event
 */
public abstract class AbstractDataEvent<TData> {
    protected final TData newValue;

    protected AbstractDataEvent(TData newValue) {
        this.newValue = newValue;
    }

    public TData getNewValue() {
        return this.newValue;
    }
}
