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

public class GetterMonad<TResult> {
    private final Function<?, TResult> getter;
    private final Supplier<TResult> defaultValueSupplier;
    private final boolean consumeCastException;

    public <TSource> GetterMonad(Function<TSource, TResult> getter) {
        this(getter, (Supplier<TResult>)null);
    }

    public <TSource> GetterMonad(Function<TSource, TResult> getter, boolean consumeCastException) {
        this(getter, consumeCastException, (Supplier<TResult>)null);
    }

    public <TModel> GetterMonad(Function<TModel, TResult> getter, Supplier<TResult> defaultValueSupplier) {
        this(getter, false, defaultValueSupplier);
    }

    public <TModel> GetterMonad(Function<TModel, TResult> getter,
                                boolean consumeCastException,
                                Supplier<TResult> defaultValueSupplier) {
        this.getter = getter;
        this.consumeCastException = consumeCastException;
        this.defaultValueSupplier = defaultValueSupplier == null ? () -> null : defaultValueSupplier;
    }

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

    public <TChainResult> GetterMonad<TChainResult> flatMap(GetterMonad<TChainResult> chainMonad) {
        return this.map(chainMonad::get, chainMonad.defaultValueSupplier);
    }

//    public <TChainResult> GetterMonad<TChainResult> map(Function<TResult, TChainResult> chainMapper,
//                                                        TChainResult defaultValue) {
//        return this.map(chainMapper, (Supplier<TChainResult>)() -> defaultValue);
//    }

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
