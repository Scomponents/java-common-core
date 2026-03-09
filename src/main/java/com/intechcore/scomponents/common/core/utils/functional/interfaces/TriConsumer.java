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

package com.intechcore.scomponents.common.core.utils.functional.interfaces;

import java.util.Objects;

/**
 * Represents an operation that accepts three input arguments and returns no result.
 * This is a functional interface whose functional method is
 * {@link #accept(Object, Object, Object)}.
 *
 * <p>This is a three‑arity specialization of {@link java.util.function.Consumer}
 *
 * @param <TFirstParam>  the type of the first argument to the operation
 * @param <TSecondParam> the type of the second argument to the operation
 * @param <TThirdParam>  the type of the third argument to the operation
 *
 * @see java.util.function.Consumer
 * @see java.util.function.BiConsumer
 */

@FunctionalInterface
public interface TriConsumer<TFirstParam, TSecondParam, TThirdParam> {
    /**
     * Performs this operation on the given arguments
     *
     * @param firstParam  the first input argument
     * @param secondParam the second input argument
     * @param thirdParam  the third input argument
     */
    void accept(TFirstParam firstParam, TSecondParam secondParam, TThirdParam thirdParam);

    /**
     * Returns a composed {@code TriConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the composed
     * operation
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code TriConsumer} that performs this operation followed
     *         by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default TriConsumer<TFirstParam, TSecondParam, TThirdParam>
    andThen(TriConsumer<? super TFirstParam, ? super TSecondParam, ? super TThirdParam> after) {
        Objects.requireNonNull(after);
        return (a, b, c) -> {
            accept(a, b, c);
            after.accept(a, b, c);
        };
    }

    /**
     * Returns a {@code TriConsumer} that does nothing (no operation).
     * This is a no‑op, equivalent to a consumer that ignores all arguments
     *
     * @param <TFirstParam>  the type of the first argument (ignored)
     * @param <TSecondParam> the type of the second argument (ignored)
     * @param <TThirdParam>  the type of the third argument (ignored)
     * @return a {@code TriConsumer} that performs no operation
     */
    static <TFirstParam, TSecondParam, TThirdParam>
    TriConsumer<TFirstParam, TSecondParam, TThirdParam> noop() {
        return (a, b, c) -> {};
    }
}
