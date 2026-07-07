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


package com.intechcore.scomponents.common.core.utils.functional;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * Extends {@link Function} with additional null-safety and composition features
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 *
 * @see Function
 */
@FunctionalInterface
public interface NsFunc<T, R> extends Function<T, R> {

    /**
     * Safely applies this function to the given argument.
     *
     * <p>If the argument is {@code null} or if the result of {@link #apply(Object)}
     * is {@code null}, this method returns {@code null}
     *
     * @param t the function argument
     * @return the function result, or {@code null} if the argument or the result is {@code null}
     */
    default R safeApply(T t) {
        return this.safeApply(t, null);
    }

    /**
     * Safely applies this function to the given argument.
     *
     * <p>If the argument is {@code null} or if the result of {@link #apply(Object)}
     * is {@code null}, this method returns the value provided by the {@code defaultValueSupplier}.
     * If the supplier is {@code null}, {@code null} is returned
     *
     * @param t the function argument
     * @param defaultValueSupplier the supplier to provide a default value if the argument
     *                             or result is {@code null}
     * @return the function result, or the default value if the argument or result is {@code null}
     */
    default R safeApply(T t, Supplier<? extends R> defaultValueSupplier) {
        if (t == null) {
            return defaultValueSupplier == null ? null : defaultValueSupplier.get();
        }

        R result = this.apply(t);
        if (result == null) {
            return defaultValueSupplier == null ? null : defaultValueSupplier.get();
        }
        return result;
    }

    /**
     * Returns a composed function that first applies this function to its input,
     * and then applies the {@code after} function to the result.
     *
     * <p>This implementation relies on {@link #then(NsFunc, Supplier)} and provides
     * a null-safe composition. If the input to this function is {@code null},
     * or if this function returns {@code null}, the {@code after} function is not
     * applied, and {@code null} is returned
     *
     * @param <V> the type of output of the {@code after} function, and of the composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then applies the {@code after} function
     * @throws NullPointerException if {@code after} is null
     */
    @Override
    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        return this.then(after::apply, null);
    }

    /**
     * Returns a composed function that first applies this function to its input,
     * and then applies the {@code after} function to the result.
     *
     * <p>This method provides a null-safe composition. If the input to this function
     * is {@code null}, or if this function returns {@code null}, the {@code after} function
     * is not applied, and {@code null} is returned
     *
     * @param <V> the type of output of the {@code after} function, and of the composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then applies the {@code after} function
     * @throws NullPointerException if {@code after} is null
     */
    default <V> NsFunc<T, V> then(NsFunc<? super R, ? extends V> after) {
        return this.then(after, null);
    }

    /**
     * Returns a composed function that first applies this function to its input,
     * and then applies the {@code after} function to the result.
     *
     * <p>If the input to this function is {@code null}, or if this function returns {@code null},
     * or if the {@code after} function returns {@code null}, the value provided by the
     * {@code defaultValueGetter} is returned. If the getter is {@code null}, {@code null} is returned
     *
     * @param <V> the type of output of the {@code after} function, and of the composed function
     * @param after the function to apply after this function is applied
     * @param defaultValueGetter the supplier to provide a default value if any step in the
     *                           composition yields {@code null}
     * @return a composed function that first applies this function and then applies the {@code after} function,
     *         returning a default value if any intermediate or final result is {@code null}
     * @throws NullPointerException if {@code after} is null
     */
    default <V> NsFunc<T, V> then(
            final NsFunc<? super R, ? extends V> after,
            final Supplier<? extends V> defaultValueGetter) {
        Objects.requireNonNull(after);
        return (T t) -> {
            R parentResult = this.safeApply(t);

//            if (after == null) {
//                if (parentResult == null) {
//                    return defaultValueGetterClosure.get();
//                }
//                try {
//                    return (V) parentResult;
//                } catch (ClassCastException ignored) {
//                    return defaultValueGetterClosure.get();
//                }
//            }

            if (parentResult == null) {
                return defaultValueGetter == null ? null : defaultValueGetter.get();
            }

            V afterResult = after.safeApply(parentResult);
            if (afterResult != null) {
                return afterResult;
            }
            return defaultValueGetter == null ? null : defaultValueGetter.get();
        };
    }
}
