package es.ucm.fdi.v3findmyroommate.ui.config;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.ucm.fdi.v3findmyroommate.R;

public class ConfigViewModel extends AndroidViewModel {

    private static SharedPreferences userPreferences;
    private static DatabaseReference databaseUserReference;

    public ConfigViewModel(Application application) {
        super(application);
        ConfigViewModel.getUserPreferences(application);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(application.
                    getApplicationContext().getString(R.string.database_url));

            ConfigViewModel.databaseUserReference = databaseInstance.getReference("users")
                    .child(user.getUid());
        }
        else {
            Log.e("UserPreferences", "Can't find current user");
            throw new NullPointerException("User wasn't found");
        }

    }


    public static SharedPreferences getUserPreferences(Application application) {
        if (ConfigViewModel.userPreferences == null)
            ConfigViewModel.userPreferences = PreferenceManager.getDefaultSharedPreferences(
                    application.getApplicationContext());
        return ConfigViewModel.userPreferences;
    }


    public static void updateSelectedPreference(String newValue, String preferenceKey, Application application) {
        SharedPreferences.Editor editor = ConfigViewModel.getUserPreferences(application).edit();

        // Updates email.
        if (preferenceKey.equals(application.getString(R.string.email_preference_key))) {
            editor.putString(application.getString(R.string.email_preference_key), newValue);
            Log.i("EmailPreference", "New email: " + newValue);
        }

        // Updates password.
        else if (preferenceKey.equals(application.getString(R.string.password_preference_key))) {
            editor.putString(application.getString(R.string.password_preference_key), newValue);
            Log.i("PasswordPreference", "New password: " + newValue);
        }

        // Updates username.
        else if (preferenceKey.equals(application.getString(R.string.username_preference_key))) {
            editor.putString(application.getString(R.string.username_preference_key), newValue);
            Log.i("UsernamePreference", "New username: " + newValue);
        }

        // Updates gender.
        else if (preferenceKey.equals(application.getString(R.string.gender_preference_key))) {
            ConfigViewModel.databaseUserReference.child(application.getString(R.string.gender_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.gender_preference_key), newValue);
            Log.i("GenderPreference", "New gender selected: " + newValue);
        }

        // Updates age range.
        else if (preferenceKey.equals(application.getString(R.string.age_range_preference_key))) {
            ConfigViewModel.databaseUserReference.child(application.getString(R.string.age_range_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.age_range_preference_key), newValue);
            Log.i("AgeRangePreference", "New age range selected: " + newValue);
        }

        // Updates marital status.
        else if (preferenceKey.equals(application.getString(R.string.marital_status_preference_key))) {
            ConfigViewModel.databaseUserReference.child(application.getString(R.string.marital_status_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.marital_status_preference_key), newValue);
            Log.i("MaritalStatusPreference", "New marital status selected: " + newValue);
        }

        // Updates occupation.
        else if (preferenceKey.equals(application.getString(R.string.occupation_preference_key))) {
            ConfigViewModel.databaseUserReference.child(application.getString(R.string.occupation_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.occupation_preference_key), newValue);
            Log.i("OccupationPreference", "New occupation selected: " + newValue);
        }

        editor.apply();
    }


    // Method that updates those user fields that require re-authentication for security reasons
    // (email and password).
    public static void updateProfile(String itemWritten, String currentPassword, UpdateCredentialsAction action, Application application) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userEmail = ConfigViewModel.getUserPreferences(application).getString(application.getString(
                R.string.email_preference_key), "");

        AuthCredential credential = EmailAuthProvider.getCredential(userEmail, currentPassword);
        if (user != null) {
            user.reauthenticate(credential).addOnCompleteListener(task -> {

                Log.d("Reauthentication", "User re-authenticated.");
                // Decides which element of the user's profile to update
                switch(action) {
                    case UPDATE_EMAIL:
                        user.updateEmail(itemWritten).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                String emailPrefKey = application.getString(R.string.email_preference_key);
                                ConfigViewModel.updateSelectedPreference(itemWritten, emailPrefKey, application);
                                Log.d("UserEmail", "User's email successfully updated");
                            }
                        });
                        break;

                    case UPDATE_PASSWORD:
                        user.updatePassword(itemWritten).addOnCompleteListener(task12 -> {
                            if (task12.isSuccessful()) {
                                String passwordPrefKey = application.getString(R.string.password_preference_key);
                                ConfigViewModel.updateSelectedPreference(itemWritten, passwordPrefKey, application);
                                Log.d("UserPassword", "User's password successfully updated");
                            }
                        });
                        break;

                    default:
                        break;
                }
            });
        }
    }


}