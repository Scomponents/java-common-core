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

package com.intechcore.scomponents.common.core.utils.functional.interfaces;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a function that accepts four arguments and produces a result.
 * This is a functional interface whose functional method is {@link #apply(Object, Object, Object, Object)}
 *
 * @param <TArgument1> the type of the first argument to the function
 * @param <TArgument2> the type of the second argument to the function
 * @param <TArgument3> the type of the third argument to the function
 * @param <TArgument4> the type of the fourth argument to the function
 * @param <TResult>    the type of the result of the function
 *
 * @see java.util.function.Function
 * @see java.util.function.BiFunction
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface FourFunction<TArgument1, TArgument2, TArgument3, TArgument4, TResult> {
    TResult apply(TArgument1 arg1, TArgument2 arg2, TArgument3 arg3, TArgument4 arg4);

    /**
     * Returns a composed function that first applies this function to its input,
     * and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function
     *
     * @param <V> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     *         applies the {@code after} function
     * @throws NullPointerException if {@code after} is null
     */
    default <V> FourFunction<TArgument1, TArgument2, TArgument3, TArgument4, V>
    andThen(Function<? super TResult, ? extends V> after) {
        Objects.requireNonNull(after);
        return (arg1, arg2, arg3, arg4) -> after.apply(apply(arg1, arg2, arg3, arg4));
    }

    /**
     * Returns a composed function that first applies the four given functions to
     * a single input {@code v} to produce four arguments, and then applies
     * this {@code FourFunction} to those arguments.
     * <p>
     * If evaluation of any of the given functions or this function throws an
     * exception, it is relayed to the caller of the composed function
     *
     * @param <V>  the type of the input to the composed function
     * @param f1   the function that produces the first argument from {@code V}
     * @param f2   the function that produces the second argument from {@code V}
     * @param f3   the function that produces the third argument from {@code V}
     * @param f4   the function that produces the fourth argument from {@code V}
     * @return a composed function that first applies the four argument‑producing
     *         functions and then applies this {@code FourFunction}
     * @throws NullPointerException if any of the argument functions are null
     */
    default <V> Function<V, TResult> compose(
            Function<? super V, ? extends TArgument1> f1,
            Function<? super V, ? extends TArgument2> f2,
            Function<? super V, ? extends TArgument3> f3,
            Function<? super V, ? extends TArgument4> f4) {
        Objects.requireNonNull(f1);
        Objects.requireNonNull(f2);
        Objects.requireNonNull(f3);
        Objects.requireNonNull(f4);
        return v -> apply(f1.apply(v), f2.apply(v), f3.apply(v), f4.apply(v));
    }

    /**
     * Returns a {@code FourFunction} that returns its first argument.
     * <p>
     * Because a true "identity" for a four‑argument function would require
     * returning a tuple of all four inputs, this method provides the next best
     * thing: a function that simply echoes the first argument. This is useful
     * as a default or placeholder in composition chains
     *
     * @param <A> the type of the first argument (and the result)
     * @param <B> the type of the second argument (ignored)
     * @param <C> the type of the third argument (ignored)
     * @param <D> the type of the fourth argument (ignored)
     * @return a four‑argument function that returns its first input
     */
    static <A, B, C, D> FourFunction<A, B, C, D, A> identity() {
        return (a, b, c, d) -> a;
    }
}
