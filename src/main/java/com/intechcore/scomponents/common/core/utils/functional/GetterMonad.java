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

package com.intechcore.scomponents.common.core.utils.functional;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A monadic wrapper for safe property access that handles null values, type casting exceptions,
 * and provides fluent chaining of getter operations.
 *
 * <p>This class encapsulates a getter function and provides safe invocation with configurable
 * exception handling and default value fallbacks. It is particularly useful for navigating
 * complex object graphs where properties may be null or of unexpected types.
 *
 * <p>Key features:
 * <ul>
 *   <li>Safe null handling with configurable default values</li>
 *   <li>Optional suppression of ClassCastExceptions during property access</li>
 *   <li>Fluent API for chaining multiple getter operations via {@link #map} and {@link #flatMap}</li>
 *   <li>Lazy evaluation of default values through Supplier interface</li>
 * </ul>
 *
 * <p>Example usage:
 * <pre>{@code
 * GetterMonad<String> nameGetter = new GetterMonad<>(Person::getName, () -> "Unknown");
 * String name = nameGetter.get(person); // Returns "Unknown" if person or getName() returns null
 *
 * // Chaining example
 * GetterMonad<Integer> ageGetter = new GetterMonad<>(Person::getAge);
 * GetterMonad<String> ageStringGetter = ageGetter.map(Object::toString, () -> "N/A");
 * }</pre>
 *
 * @param <TResult> the type of the result produced by the getter function
 */
public class GetterMonad<TResult> {
    private final Function<?, TResult> getter;
    private final Supplier<TResult> defaultValueSupplier;
    private final boolean consumeCastException;

    /**
     * Constructs a GetterMonad with the specified getter function and a null default value
     *
     * @param <TSource> the type of the source object for the getter function
     * @param getter the function to extract the value from the source object
     */
    public <TSource> GetterMonad(Function<TSource, TResult> getter) {
        this(getter, (Supplier<TResult>)null);
    }

    /**
     * Constructs a GetterMonad with the specified getter function and cast exception behavior
     *
     * @param <TSource> the type of the source object for the getter function
     * @param getter the function to extract the value from the source object
     * @param consumeCastException if true, ClassCastExceptions will be caught and treated as null
     */
    public <TSource> GetterMonad(Function<TSource, TResult> getter, boolean consumeCastException) {
        this(getter, consumeCastException, (Supplier<TResult>)null);
    }

    /**
     * Constructs a GetterMonad with the specified getter function and default value supplier
     *
     * @param <TModel> the type of the source object for the getter function
     * @param getter the function to extract the value from the source object
     * @param defaultValueSupplier supplier for a default value when getter returns null
     */
    public <TModel> GetterMonad(Function<TModel, TResult> getter, Supplier<TResult> defaultValueSupplier) {
        this(getter, false, defaultValueSupplier);
    }

    /**
     * Constructs a GetterMonad with full configuration options
     *
     * @param <TModel> the type of the source object for the getter function
     * @param getter the function to extract the value from the source object
     * @param consumeCastException if true, ClassCastExceptions will be caught and treated as null
     * @param defaultValueSupplier supplier for a default value when getter returns null or
     *                            (if consumeCastException is true) when a ClassCastException occurs
     */
    public <TModel> GetterMonad(Function<TModel, TResult> getter,
                                boolean consumeCastException,
                                Supplier<TResult> defaultValueSupplier) {
        this.getter = getter;
        this.consumeCastException = consumeCastException;
        this.defaultValueSupplier = defaultValueSupplier == null ? () -> null : defaultValueSupplier;
    }

    /**
     * Applies the getter function to the provided model object safely
     *
     * <p>The method follows this execution flow:
     * <ol>
     *   <li>If the getter is null, returns the default value</li>
     *   <li>Applies the getter to the model, optionally handling ClassCastExceptions</li>
     *   <li>If the result is null, returns the default value</li>
     *   <li>Otherwise returns the getter result</li>
     * </ol>
     *
     * @param <TModel> the type of the source object
     * @param model the source object to apply the getter to
     * @return the result of the getter application, or the default value if the getter
     *         returns null or cannot be applied
     */
    public <TModel> TResult get(TModel model) {
        if (this.getter == null) {
            return this.defaultValueSupplier.get();
        }

        TResult result = this.consumeCastException
                ? this.applyIgnoredCastException(model)
                : this.applyWithCastException(model);

        if (result == null) {
            return this.defaultValueSupplier.get();
        }
        return result;
    }

    private <TModel> TResult applyIgnoredCastException(TModel model) {
        try {
            return this.applyWithCastException(model);
        } catch (ClassCastException ignored) {

        }
        return null;
    }

    private <TModel> TResult applyWithCastException(TModel model) {
        return ((Function<TModel, TResult>)this.getter).apply(model);
    }

    public <TChainResult> GetterMonad<TChainResult> map(Function<TResult, TChainResult> chainMapper) {
        return this.map(chainMapper, (Supplier<TChainResult>)null);
    }

    /**
     * Creates a new GetterMonad by flat-mapping with another GetterMonad
     *
     * <p>This is equivalent to mapping with {@link GetterMonad#get} but preserves the
     * default value supplier of the chained monad
     *
     * @param <TChainResult> the result type of the chained monad
     * @param chainMonad the monad to flat-map with
     * @return a new GetterMonad that applies the chained monad to this getter's result
     * @throws NullPointerException if chainMonad is null
     */
    public <TChainResult> GetterMonad<TChainResult> flatMap(GetterMonad<TChainResult> chainMonad) {
        return this.map(chainMonad::get, chainMonad.defaultValueSupplier);
    }

    /**
     * Creates a new GetterMonad by applying a mapping function to the result of this getter
     *
     * <p>This method enables fluent chaining of getter operations. The new GetterMonad will
     * inherit the cast exception behavior of the current monad.
     *
     * @param <TChainResult> the result type of the mapping function
     * @param chainMapper the function to apply to the result of this getter
     * @param chainDefaultValueSupplier supplier for a default value when getter returns null or
     *                            (if consumeCastException is true) when a ClassCastException occurs
     * @return a new GetterMonad that applies the mapping function to this getter's result
     */
    public <TChainResult> GetterMonad<TChainResult> map(
            Function<TResult, TChainResult> chainMapper,
            Supplier<TChainResult> chainDefaultValueSupplier) {
        final Supplier<TChainResult> chainDefaultValueSupplierClosure = chainDefaultValueSupplier == null
                ? () -> null
                : chainDefaultValueSupplier;
        return new GetterMonad<TChainResult>(tModel -> {

            TResult currentResult = this.get(tModel);

            if (currentResult == null) {
                return chainDefaultValueSupplierClosure.get();
            }

            return chainMapper.apply(currentResult);
        }, this.consumeCastException, chainDefaultValueSupplier);
    }
}
