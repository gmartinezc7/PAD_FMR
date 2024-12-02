package es.ucm.fdi.v3findmyroommate;

import java.util.HashMap;
import java.util.Map;

public class TranslationUtils {
    private static final Map<String, String> translationMap = new HashMap<>();

    static {
        // Mapeos de traducción (puedes extender esta lista)
        translationMap.put("House", "Casa");
        translationMap.put("Room", "Habitación");
        translationMap.put("Apartment", "Apartamento");
        translationMap.put("Home", "Casa independiente");
        translationMap.put("Male", "Hombre");
        translationMap.put("Woman", "Mujer");
        translationMap.put("Both", "Mixto");
        translationMap.put("Exterior", "Exterior");
        translationMap.put("Interior", "Interior");
        translationMap.put("Private", "Privado");
        translationMap.put("Shared", "Compartido");



        // Añade más traducciones según necesites
    }

    public static String translateToBaseLanguage(String localizedValue) {
        return translationMap.getOrDefault(localizedValue, localizedValue);
    }
}
