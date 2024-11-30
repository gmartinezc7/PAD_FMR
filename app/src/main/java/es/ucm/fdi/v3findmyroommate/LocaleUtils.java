package es.ucm.fdi.v3findmyroommate;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocaleUtils {

    private static List<Locale> localesList;


    // Singleton function to access the list of existing locales.
    private static List<Locale> getLocalesList() {
        if (localesList == null) {
            localesList = new ArrayList<>();
        }
        return localesList;
    }


    // Method that adds the locale to the list of locales.
    public static void addLocale(String localeName) {
        Locale newLocaleToAdd = new Locale(localeName);
        getLocalesList().add(newLocaleToAdd);
    }


    // Method that sets the default locale.
    public static void setDefaultLocale(Context context, String localeName) {

        // Creates a new locale from the string parameter.
        Locale newDefaultLocale = new Locale((localeName));

        // Updates the app's configuration with the new locale.
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(newDefaultLocale);

        // Updates the default locale for the app.
        Locale.setDefault(newDefaultLocale);

        // Applies the configuration to the app context.
        context.getApplicationContext().createConfigurationContext(config);
    }


    // Function that returns the value that the given key has in the locale used in the DB.
    public static String getValueInDBLocale(Context context, int keyId) {
        String result = "";
        Resources initialResources = context.getResources();

        for (Locale locale : getLocalesList()) {
            if (locale.equals(new Locale("es"))) {
                Configuration config = new Configuration(initialResources.getConfiguration());
                config.setLocale(locale);

                // Creates context with the updated configuration.
                Context localizedContext = context.createConfigurationContext(config);

                // Gets the updated resources.
                Resources resources = localizedContext.getResources();
                String localizedValue;

                // Gets the string value associated with the given key ID in the current locale.
                result = resources.getString(keyId);

                return result;
            }
        }
        return result;
/*
        // Gets the default locale.
        Locale defaultLocale = Locale.getDefault();

        // Creates a configuration for the default locale.
        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(defaultLocale);

        // Creates a context with the updated configuration.
        Context localizedContext = context.createConfigurationContext(config);

        // Fetches the string value using the default locale.
        return localizedContext.getResources().getString(keyId);

 */
    }


    // Function that returns whether the value
    public static boolean doesStringMatchAnyLanguage(Context context, String stringToMatch, int keyId) {

        Resources initialResources = context.getResources();

        for (Locale locale : getLocalesList()) {
            Configuration config = new Configuration(initialResources.getConfiguration());
            config.setLocale(locale);

            // Creates context with the updated configuration.
            Context localizedContext = context.createConfigurationContext(config);

            // Gets the updated resources.
            Resources resources = localizedContext.getResources();
            String localizedValue;

            // Gets the string value associated with the given key ID in the current locale.
            localizedValue = resources.getString(keyId);

            if (stringToMatch.equals(localizedValue)) {
                return true; // If the string of the key matches the current locale-key value.
            }
        }

        return false; // In case no match was found.
    }


}
