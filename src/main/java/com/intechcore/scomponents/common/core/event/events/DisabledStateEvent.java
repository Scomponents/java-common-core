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
 * The main class of all events that are responsible for the performance of certain operations.
 * <p>For example, if there are no comments in a cell, operation 'delete comment' is meaningless
 */
public abstract class DisabledStateEvent {
    private final Boolean disabled;

    protected DisabledStateEvent(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getDisabled() {
        return this.disabled;
    }
}
