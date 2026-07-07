package com.intechcore.scomponents.common.core.i18n;

/**
 * Defines the contract for an internationalization (i18n) service that resolves
 * {@link II18nKey} instances to localized strings.
 * <p>
 * This service manages translation resource bundles, provides translation lookup using
 * either the default or a specified language, and allows runtime language switching.
 * </p>
 */
public interface II18nService {

    /**
     * Translates an II18nKey into a string using the current default language.
     *
     * @param key the internationalization key to translate
     * @return the translated string, or the key itself if no translation is found
     */
    String translate(II18nKey key);

    /**
     * Translates an II18nKey into a string using the specified language.
     *
     * @param key the internationalization key to translate
     * @param languageTag the language tag to use for translation (e.g., "en", "de", "fr-FR")
     * @return the translated string, or the key itself if no translation is found for the given language
     */
    String translate(II18nKey key, String languageTag);

    /**
     * Adds a translation resource bundle to the service.
     * <p>
     * The bundle will be loaded and its translations made available for lookups.
     * </p>
     *
     * @param bundleName the base name of the resource bundle (e.g., "messages", "com.example.Messages")
     */
    void addTranslationsResource(String bundleName);

    /**
     * Loads all registered translation resource bundles.
     * <p>
     * This method attempts to load and initialize all bundles that have been added
     * via {@link #addTranslationsResource(String)}.
     * </p>
     *
     * @return {@code true} if all bundles were loaded successfully, {@code false} otherwise
     */
    boolean loadAllBundles();

    /**
     * Sets the current default language for translations.
     *
     * @param languageTag the language tag to use as the default (e.g., "en", "de", "fr-FR")
     */
    void setLanguage(String languageTag);

    /**
     * Gets the currently active default language.
     *
     * @return the current language tag
     */
    String getCurrentLanguage();
}