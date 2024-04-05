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

package com.intechcore.scomponents.common.core.utils;

@FunctionalInterface
public interface TriConsumer<TFirstParam, TSecondParam, TThirdParam> {
    void accept(TFirstParam firstParam, TSecondParam secondParam, TThirdParam thirdParam);
}
