package com.intechcore.scomponents.common.core.i18n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Defines an internationalization (i18n) key used for message translation
 * <p>
 * This functional interface provides a type-safe wrapper around message keys with
 * support for positional and named arguments. Keys can be created from string literals,
 * enum values, or with additional parameters for message formatting
 * <p>
 * Implementations are typically created via the static factory methods in this interface,
 * most commonly {@link #create(String, Object...)}, {@link #createForEnum(Enum, Object...)},
 * or {@link #createWithArgs(String, Map, Object...)}
 * </p>
 */
@FunctionalInterface
public interface II18nKey {

    /**
     * Creates a new II18nKey with the specified key and positional arguments.
     *
     * @param key   the message key
     * @param args  positional arguments for message formatting
     * @return a new II18nKey instance
     */
    static II18nKey create(String key, Object... args) {
        return new Key(key, args, Collections.emptyMap());
    }

    /**
     * Creates a new II18nKey from an enum value.
     * <p>
     * The key is formatted as "EnumClassName.ENUM_VALUE".
     * </p>
     *
     * @param source the enum value to create a key from
     * @param args   positional arguments for message formatting
     * @return a new II18nKey instance
     */
    static II18nKey createForEnum(Enum<?> source, Object... args) {
        String key = source.getClass().getSimpleName() + "." + source.name();
        return create(key, args);
    }

    /**
     * Creates a new II18nKey with the specified key and named arguments.
     *
     * @param key       the message key
     * @param namedArgs named arguments for message formatting
     * @return a new II18nKey instance
     */
    static II18nKey create(String key, Map<String, Object> namedArgs) {
        return new Key(key, new Object[0], namedArgs);
    }

    /**
     * Creates a new II18nKey with the specified key, named arguments, and positional arguments.
     *
     * @param key         the message key
     * @param namedArgs   named arguments for message formatting
     * @param args        positional arguments for message formatting
     * @return a new II18nKey instance
     */
    static II18nKey createWithArgs(String key, Map<String, Object> namedArgs, Object... args) {
        return new Key(key, args, namedArgs);
    }

    /**
     * Wraps a key in the standard internationalization key format.
     *
     * @param key the key to wrap
     * @return the wrapped key in the format "$(key)"
     */
    static String wrapKey(String key) {
        return "$(" + key + ")";
    }

    /**
     * Gets the internationalization key.
     *
     * @return the key used for looking up translated messages
     */
    String getI18nKey();

    /**
     * Gets the named parameters for message formatting.
     * <p>
     * Default implementation returns null indicating no named parameters.
     * </p>
     *
     * @return the named parameters map, or null if none
     */
    default Map<String, Object> getParams() {
        return null;
    }

    /**
     * Gets the positional arguments for message formatting.
     * <p>
     * Default implementation returns null indicating no positional arguments.
     * </p>
     *
     * @return the positional arguments array, or null if none
     */
    default Object[] getArgs() {
        return null;
    }

    /**
     * Creates a new II18nKey based on this key with additional parameters and arguments.
     *
     * @param withParams additional named parameters to include
     * @param args       additional positional arguments to include
     * @return a new II18nKey instance with combined parameters and arguments
     */
    default II18nKey getWith(Map<String, Object> withParams, Object... args) {
        Map<String, Object> resultParams = new HashMap<>();
        Map<String, Object> existingParams = this.getParams();
        if (existingParams != null) {
            resultParams.putAll(existingParams);
        }
        if (withParams != null) {
            resultParams.putAll(withParams);
        }
        List<Object> resultArgs = new ArrayList<>();
        Object[] existingArgs = this.getArgs();
        if (existingArgs != null) {
            resultArgs.addAll(Arrays.asList(existingArgs));
        }
        resultArgs.addAll(Arrays.asList(args));
        return createWithArgs(this.getI18nKey(), resultParams, resultArgs);
    }

    /**
     * Concrete implementation of II18nKey that holds key information.
     * <p>
     * This class is immutable and thread-safe.
     * </p>
     */
    final class Key implements II18nKey {
        private final String key;
        private final Object[] args;
        private final Map<String, Object> namedArgs;

        /**
         * Creates a new Key instance.
         *
         * @param key       the message key
         * @param args      positional arguments for message formatting
         * @param namedArgs named arguments for message formatting
         */
        private Key(String key, Object[] args, Map<String, Object> namedArgs) {
            this.key = key;
            this.args = (args == null)
                    ? new Object[0]
                    : args.clone();
            this.namedArgs = (namedArgs == null)
                    ? Collections.emptyMap()
                    : Collections.unmodifiableMap(namedArgs);
        }

        /**
         * Gets the internationalization key.
         *
         * @return the key used for looking up translated messages
         */
        @Override
        public String getI18nKey() {
            return this.key;
        }

        /**
         * Gets the named parameters for message formatting.
         *
         * @return the named parameters map (unmodifiable)
         */
        @Override
        public Map<String, Object> getParams() {
            return this.namedArgs;
        }

        /**
         * Gets the positional arguments for message formatting.
         *
         * @return the positional arguments array
         */
        @Override
        public Object[] getArgs() {
            return this.args;
        }

        /**
         * Indicates whether some other object is "equal to" this one.
         *
         * @param o the reference object with which to compare
         * @return true if this object is the same as the obj argument; false otherwise
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key other = (Key) o;

            return Objects.equals(this.key, other.key) &&
                    Arrays.equals(this.args, other.args) &&
                    Objects.equals(this.namedArgs, other.namedArgs);
        }

        /**
         * Returns a hash code value for the object.
         *
         * @return a hash code value for this object
         */
        @Override
        public int hashCode() {
            int result = this.key != null ? this.key.hashCode() : 0;
            result = 31 * result + Arrays.hashCode(this.args);
            result = 31 * result + this.namedArgs.hashCode();
            return result;
        }
    }
}