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

package com.intechcore.scomponents.common.core.exceptions;

/**
 * A utility class providing helper methods for exception handling and analysis
 *
 * <p>This class contains static methods for working with exceptions, such as traversing
 * exception cause chains and extracting root cause information
 *
 * <p>This class cannot be instantiated and is designed to be used as a static utility
 */
public final class ExceptionUtils {

    /**
     * This class is a static utility class and should not be instantiated
     */
    private ExceptionUtils() {
    }

    /**
     * Recursively traverses the exception cause chain to find the root cause of the given throwable
     *
     * <p>This method follows the chain of {@link Throwable#getCause()} recursively until it reaches
     * an exception that has no cause. The root cause is the initial exception that triggered the chain.
     *
     * <p><b>Behavior:</b>
     * <ul>
     *   <li>If the input throwable is {@code null}, returns {@code null}</li>
     *   <li>If the throwable has no cause, returns the throwable itself</li>
     *   <li>Otherwise, recursively follows the cause chain until the root cause is found</li>
     * </ul>
     *
     * <p><b>Example cause chain traversal:</b>
     * <pre>{@code
     * // Given: ExceptionA → ExceptionB → ExceptionC (root cause)
     * Throwable root = ExceptionUtils.getRootCause(ExceptionA);
     * // Returns: ExceptionC
     * }</pre>
     *
     * @param throwable the throwable to analyze; may be {@code null}
     * @return the root cause of the throwable, or the throwable itself if it has no cause,
     *         or {@code null} if the input is {@code null}
     *
     * @see Throwable#getCause()
     */
    public static Throwable getRootCause(Throwable throwable) {
        Throwable result = throwable.getCause();
        if (result != null) {
            return getRootCause(result);
        }
        return throwable;
    }
}
