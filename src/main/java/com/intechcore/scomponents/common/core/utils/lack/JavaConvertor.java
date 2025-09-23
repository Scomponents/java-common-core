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


package com.intechcore.scomponents.common.core.utils.lack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A utility class providing conversion methods for common Java type transformations
 *
 * <p>This class is designed as a static utility class and cannot be instantiated.
 * All methods are static and thread-safe
 */
public final class JavaConvertor {

    /**
     * This class is a static utility class and should not be instantiated
     */
    private JavaConvertor() {
    }

    /**
     * Converts an {@link InputStream} to a byte array using a default buffer size of 1024 bytes
     *
     * <p>This method reads all bytes from the input stream until end-of-stream is reached.
     * The input stream is not closed by this method - the caller is responsible for closing it
     *
     * <p><b>Performance note:</b> For large streams, consider using {@link #toByteArray(InputStream, int)}
     * with an optimized buffer size (typically 4KB-8KB for better performance)
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * try (InputStream is = new FileInputStream("data.bin")) {
     *     byte[] bytes = JavaConvertor.toByteArray(is);
     *     System.out.println("Read " + bytes.length + " bytes");
     * }
     * }</pre>
     *
     * @param inputStream the input stream to read from; must not be null
     * @return a byte array containing all bytes read from the input stream
     * @throws IOException if an I/O error occurs during reading
     * @throws NullPointerException if the inputStream is null
     * @see #toByteArray(InputStream, int)
     */
    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        return toByteArray(inputStream, 1024);
    }

    /**
     * Converts an {@link InputStream} to a byte array using a default buffer size of 1024 bytes
     *
     * <p>This method reads all bytes from the input stream until end-of-stream is reached.
     * The input stream is not closed by this method - the caller is responsible for closing it
     *
     * <p><b>Performance note:</b> For large streams, consider using {@link #toByteArray(InputStream, int)}
     * with an optimized buffer size (typically 4KB-8KB for better performance)
     *
     * <p><b>Example:</b>
     * <pre>{@code
     * try (InputStream is = new FileInputStream("data.bin")) {
     *     byte[] bytes = JavaConvertor.toByteArray(is);
     *     System.out.println("Read " + bytes.length + " bytes");
     * }
     * }</pre>
     *
     * @param inputStream the input stream to read from; must not be null
     * @param bufferSize custom buffer size
     * @return a byte array containing all bytes read from the input stream
     * @throws IOException if an I/O error occurs during reading
     * @throws NullPointerException if the inputStream is null
     * @see #toByteArray(InputStream, int)
     */
    public static byte[] toByteArray(InputStream inputStream, int bufferSize) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[bufferSize];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        return outputStream.toByteArray();
    }
}
