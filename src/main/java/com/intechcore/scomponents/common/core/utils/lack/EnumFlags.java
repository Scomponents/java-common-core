/*
 * Copyright (c) 2026-present, Intechcore GmbH
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


package com.intechcore.scomponents.common.core.utils.lack;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * A fluent, type‑safe wrapper around an {@link EnumSet} that models a set of enum constants
 * as binary flags. Provides chainable mutation methods and various query operations
 *
 * <p>All mutating methods return {@code this} to enable method chaining.
 * Instances are <strong>not</strong> thread‑safe; external synchronization is required
 * if accessed concurrently
 *
 * @param <TEnum> the enum type whose constants are used as flags; must extend {@link Enum}
 *
 * @see EnumSet
 * @see Enum
 */
public class EnumFlags<TEnum extends Enum<TEnum>> {
    private final EnumSet<TEnum> flags;
    private final Class<TEnum> sourceEnum;

    /**
     * Constructs an empty {@code EnumFlags} instance for the given enum type
     *
     * @param source the {@code Class} object of the enum type
     * @throws NullPointerException if {@code source} is {@code null}
     */
    public EnumFlags(Class<TEnum> source) {
        this.sourceEnum = source;
        this.flags = EnumSet.noneOf(source);
    }

    /**
     * Creates a new, independent {@code EnumFlags} instance for the same enum type,
     * containing no flags. Useful for resetting state or building a fresh flag set
     *
     * @return a new empty {@code EnumFlags} instance for the same enum type
     */
    public EnumFlags<TEnum> createEmpty() {
        return new EnumFlags<>(this.sourceEnum);
    }

    /**
     * Checks whether the specified flag is currently present
     *
     * @param flag the enum constant to test
     * @return {@code true} if the flag is contained, otherwise {@code false}
     * @throws NullPointerException if {@code flag} is {@code null}
     */
    public boolean contains(TEnum flag) {
        return this.flags.contains(flag);
    }

    /**
     * Checks whether all of the given flags are currently present
     *
     * @param flags one or more enum constants to test
     * @return {@code true} if every specified flag is contained, otherwise {@code false}
     * @throws NullPointerException if the array itself or any individual flag is {@code null}
     */
    @SafeVarargs
    public final boolean containsAll(TEnum... flags) {
        return this.flags.containsAll(Arrays.asList(flags));
    }

    /**
     * Checks whether at least one of the given flags is currently present
     *
     * @param flags one or more enum constants to test
     * @return {@code true} if any of the specified flags is contained, otherwise {@code false}
     * @throws NullPointerException if the array or any individual flag is {@code null}
     */
    @SafeVarargs
    public final boolean containsAny(TEnum... flags) {
        for (final TEnum flag : flags) {
            if (this.contains(flag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a mutable copy of the currently contained flags.
     * Changes to the returned set do not affect this {@code EnumFlags}
     * instance, and vice versa
     *
     * @return a new {@link HashSet} containing all flags currently present
     */
    public Set<TEnum> allContained() {
        return new HashSet<>(this.flags);
    }

    /**
     * Adds the specified flag to the set
     *
     * @param flag the enum constant to add
     * @return {@code this} instance for method chaining
     * @throws NullPointerException if {@code flag} is {@code null}
     */
    public EnumFlags<TEnum> set(TEnum flag) {
        this.flags.add(flag);
        return this;
    }

    /**
     * Adds multiple flags to the set
     *
     * @param flags one or more enum constants to add
     * @return {@code this} instance for method chaining
     * @throws NullPointerException if the array or any individual flag is {@code null}
     */
    @SafeVarargs
    public final EnumFlags<TEnum> set(TEnum... flags) {
        Arrays.stream(flags).forEach(this::set);
        return this;
    }

    /**
     * Adds all constants of the enum type to the set.
     * After calling this method, {@link #containsAll(Enum[])} will return {@code true}
     * for every enum constant
     *
     * @return {@code this} instance for method chaining
     */
    public EnumFlags<TEnum> setAll() {
        this.flags.addAll(EnumSet.allOf(this.sourceEnum));
        return this;
    }
}
