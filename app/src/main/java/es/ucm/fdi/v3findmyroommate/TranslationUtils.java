package es.ucm.fdi.v3findmyroommate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TranslationUtils {
    private static final Map<String, String> translationMap = new HashMap<>();
    private static final Map<String, String> reverseTranslationMap = new HashMap<>();


    static {
        // Mapeos de traducción (puedes extender esta lista)
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

    private static String translateToBaseLanguage(String localizedValue) {
        return translationMap.getOrDefault(localizedValue, localizedValue);
    }

    public static String translateIfNeeded(String value) {
        Locale currentLocale = Locale.getDefault();
        boolean isSpanish = currentLocale.getLanguage().equals("es");

        if (!isSpanish && value != null) {
            return translateToBaseLanguage(value);
        }
        return value;
    }

    public static String reverseTranslateIfNeeded(String value) {
        Locale currentLocale = Locale.getDefault();
        boolean isEnglish = currentLocale.getLanguage().equals("en");
        if (isEnglish && value != null) {
            return reverseTranslationMap.getOrDefault(value, value);
        }
        return value;

    }
}
