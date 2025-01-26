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

package com.intechcore.scomponents.common.core.utils.comparator;

import java.util.Comparator;

/**
 * Created to compare class types between each other and such other objects ({@code Runnable}s) to use with appropriate {@code TreeMap}s
 * @param <TItem> type of the target objects to be compared
 */
public class IdentityComparator<TItem> implements Comparator<TItem> {
    @Override
    public int compare(TItem o1, TItem o2) {
        return Integer.compare(System.identityHashCode(o1), System.identityHashCode(o2));
    }
}
