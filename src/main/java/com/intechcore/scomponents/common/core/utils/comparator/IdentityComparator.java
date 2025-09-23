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
 * A comparator that compares objects based on their identity hash code rather than
 * their natural ordering or equals implementation
 *
 * <p>This comparator uses {@link System#identityHashCode(Object)} to compare objects,
 * which provides a hash code based on the object's memory address rather than its
 * content. This is useful when you need to maintain objects in collections based on
 * their identity rather than their value.
 *
 * <p><b>Important characteristics:</b>
 * <ul>
 *   <li>Not consistent with {@link Object#equals(Object)} - two distinct objects
 *       that are equal by their {@code equals()} method may be ordered differently</li>
 *   <li>Useful for identity-based ordering in collections like {@link java.util.TreeMap}</li>
 *   <li>Particularly helpful for objects that don't implement {@link Comparable}
 *       but need to be stored in sorted collections</li>
 *   <li>Can be used with {@link Runnable} and other objects where identity matters
 *       more than content</li>
 * </ul>
 *
 * <p><b>Example usage:</b>
 * <pre>{@code
 * TreeMap<Runnable, String> taskMap = new TreeMap<>(new IdentityComparator<>());
 * Runnable task1 = () -> System.out.println("Task 1");
 * Runnable task2 = () -> System.out.println("Task 2");
 *
 * taskMap.put(task1, "First task");
 * taskMap.put(task2, "Second task");
 * // The tasks are ordered by their identity, not by their content
 * }</pre>
 *
 * <p><b>Note:</b> While identity hash codes are typically unique, collisions can occur
 * in rare cases. This comparator should not be used in scenarios where absolute
 * uniqueness is required.
 *
 * @param <TItem> the type of objects that may be compared by this comparator
 *
 * @see System#identityHashCode(Object)
 * @see Comparator
 * @see java.util.TreeMap
 */
public class IdentityComparator<TItem> implements Comparator<TItem> {
    /**
     * Compares two objects based on their identity hash codes
     *
     * <p>This method returns:
     * <ul>
     *   <li>A negative integer if the first object's identity hash code is less than the second's</li>
     *   <li>Zero if both objects have the same identity hash code (typically the same object)</li>
     *   <li>A positive integer if the first object's identity hash code is greater than the second's</li>
     * </ul>
     *
     * <p><b>Important:</b> Returning zero does not necessarily mean the objects are identical,
     * as identity hash code collisions can occur, though they are rare.
     *
     * @param o1 the first object to be compared
     * @param o2 the second object to be compared
     * @return a negative integer, zero, or a positive integer as the first object's
     *         identity hash code is less than, equal to, or greater than the second's
     * @see Integer#compare(int, int)
     */
    @Override
    public int compare(TItem o1, TItem o2) {
        return Integer.compare(System.identityHashCode(o1), System.identityHashCode(o2));
    }
}
