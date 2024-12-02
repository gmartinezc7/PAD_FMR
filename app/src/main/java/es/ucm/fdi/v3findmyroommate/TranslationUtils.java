package es.ucm.fdi.v3findmyroommate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Utility class for managing translations between English and Spanish for predefined terms.
 * This class provides methods to translate terms based on the current locale and handles
 * both forward and reverse translations.
 */

public class TranslationUtils {
    private static final Map<String, String> translationMap = new HashMap<>();
    private static final Map<String, String> reverseTranslationMap = new HashMap<>();

    // Maps for storing English-to-Spanish and Spanish-to-English translations.

    static {
        // Translation mappings from English to Spanish.
        translationMap.put("House", "Casa");
        translationMap.put("Room", "Habitación");
        translationMap.put("Apartment", "Apartamento");
        translationMap.put("Home", "Casa independiente");
        translationMap.put("Man", "Hombre");
        translationMap.put("Woman", "Mujer");
        translationMap.put("Both", "Mixto");
        translationMap.put("Exterior", "Exterior");
        translationMap.put("Interior", "Interior");
        translationMap.put("Private", "Privado");
        translationMap.put("Shared", "Compartido");
        // Reverse translation mappings from Spanish to English.

        reverseTranslationMap.put("Casa", "House");
        reverseTranslationMap.put("Habitación", "Room");
        reverseTranslationMap.put("Apartamento", "Apartment");
        reverseTranslationMap.put("Casa independiente", "Home");
        reverseTranslationMap.put("Hombre", "Man");
        reverseTranslationMap.put("Mujer", "Woman");
        reverseTranslationMap.put("Mixto", "Both");
        reverseTranslationMap.put("Exterior", "Exterior");
        reverseTranslationMap.put("Interior", "Interior");
        reverseTranslationMap.put("Privado", "Private");
        reverseTranslationMap.put("Compartido", "Shared");


        // Añade más traducciones según necesites
    }

    /**
     * Translates a given value from English to Spanish, or returns the original value
     * if no translation is found.
     *
     * @param localizedValue The value to translate.
     * @return The translated value or the original if no translation exists.
     */

    private static String translateToBaseLanguage(String localizedValue) {
        return translationMap.getOrDefault(localizedValue, localizedValue);
    }

    /**
     * Translates the given value to the base language (English) if the current locale is not Spanish.
     * If the locale is Spanish, the original value is returned.
     *
     * @param value The value to be translated if needed.
     * @return The translated value or the original based on the locale.
     */

    public static String translateIfNeeded(String value) {
        Locale currentLocale = Locale.getDefault();
        boolean isSpanish = currentLocale.getLanguage().equals("es");

        if (!isSpanish && value != null) {
            return translateToBaseLanguage(value);
        }
        return value;
    }

    /**
     * Translates the given value to Spanish if the current locale is English.
     * If the locale is not English, the original value is returned.
     *
     * @param value The value to be reverse-translated if needed.
     * @return The reverse-translated value or the original based on the locale.
     */
    public static String reverseTranslateIfNeeded(String value) {
        Locale currentLocale = Locale.getDefault();
        boolean isEnglish = currentLocale.getLanguage().equals("en");
        if (isEnglish && value != null) {
            return reverseTranslationMap.getOrDefault(value, value);
        }
        return value;

    }
}
