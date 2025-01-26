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

/**
 * {@code Supplier} with checked exception
 * @param <TResult> type of the result
 * @param <TException> type of the checked exception
 */
@FunctionalInterface
public interface ExceptionSupplier<TResult, TException extends Exception> {
    TResult get() throws TException;
}
