package es.ucm.fdi.v3findmyroommate.ui.config;

import android.content.SharedPreferences;
import android.content.Context;

public class PreferenceInitializationRepository {
    private static final String PREFS_NAME = "UserPreferences";
    private SharedPreferences sharedPreferences;

    public PreferenceInitializationRepository(Context context) {
        this.sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
    }

    public void setDefaultValues(String username, String email, String password, String description) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!sharedPreferences.contains("username_preference")) {
            editor.putString("username_preference", username);
        }
        if (!sharedPreferences.contains("email_preference")) {
            editor.putString("email_preference", email);
        }
        if (!sharedPreferences.contains("password_preference")) {
            editor.putString("password_preference", password);
        }
        if (!sharedPreferences.contains("description_preference")) {
            editor.putString("description_preference", description);
        }
        editor.apply();
    }
}
