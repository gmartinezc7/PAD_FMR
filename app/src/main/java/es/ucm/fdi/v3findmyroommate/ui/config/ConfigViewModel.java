package es.ucm.fdi.v3findmyroommate.ui.config;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.ucm.fdi.v3findmyroommate.R;

public class ConfigViewModel extends AndroidViewModel {

    private static SharedPreferences userPreferences;
    private static Application application;
    private static DatabaseReference databaseUserReference;

    public ConfigViewModel(Application application) {
        super(application);
        ConfigViewModel.application = application;
        ConfigViewModel.getUserPreferences();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(ConfigViewModel.application.
                    getApplicationContext().getString(R.string.database_url));

            ConfigViewModel.databaseUserReference = databaseInstance.getReference("users")
                    .child(user.getUid());
        }
        else {
            Log.e("UserPreferences", "Can't find current user");
            throw new NullPointerException("User found to be null");
        }
    }


    public static SharedPreferences getUserPreferences() {
        if (ConfigViewModel.userPreferences == null)
            ConfigViewModel.userPreferences = PreferenceManager.getDefaultSharedPreferences(
                    ConfigViewModel.application.getApplicationContext());
        return ConfigViewModel.userPreferences;
    }


    public static void updateSelectedPreference(String newValue, String preferenceKey) {
        SharedPreferences.Editor editor = ConfigViewModel.getUserPreferences().edit();

        // Updates email.
        if (preferenceKey.equals(ConfigViewModel.application.getString(R.string.email_preference_key))) {
            editor.putString(ConfigViewModel.application.getString(R.string.email_preference_key), newValue);
            Log.i("EmailPreference", "New email: " + newValue);
        }

        // Updates password.
        else if (preferenceKey.equals(ConfigViewModel.application.getString(R.string.password_preference_key))) {
            editor.putString(ConfigViewModel.application.getString(R.string.password_preference_key), newValue);
            Log.i("PasswordPreference", "New password: " + newValue);
        }

        // Updates username.
        else if (preferenceKey.equals(ConfigViewModel.application.getString(R.string.username_preference_key))) {
            editor.putString(ConfigViewModel.application.getString(R.string.username_preference_key), newValue);
            Log.i("UsernamePreference", "New username: " + newValue);
        }

        // Updates gender.
        else if (preferenceKey.equals(ConfigViewModel.application.getString(R.string.gender_preference_key))) {
            ConfigViewModel.databaseUserReference.child(ConfigViewModel.application.getString(R.string.gender_db_label)).setValue(newValue);
            editor.putString(ConfigViewModel.application.getString(R.string.gender_preference_key), newValue);
            Log.i("GenderPreference", "New gender selected: " + newValue);
        }

        // Updates age range.
        else if (preferenceKey.equals(ConfigViewModel.application.getString(R.string.age_range_preference_key))) {
            ConfigViewModel.databaseUserReference.child(ConfigViewModel.application.getString(R.string.age_range_db_label)).setValue(newValue);
            editor.putString(ConfigViewModel.application.getString(R.string.age_range_preference_key), newValue);
            Log.i("AgeRangePreference", "New age range selected: " + newValue);
        }

        // Updates marital status.
        else if (preferenceKey.equals(ConfigViewModel.application.getString(R.string.marital_status_preference_key))) {
            ConfigViewModel.databaseUserReference.child(ConfigViewModel.application.getString(R.string.marital_status_db_label)).setValue(newValue);
            editor.putString(ConfigViewModel.application.getString(R.string.marital_status_preference_key), newValue);
            Log.i("MaritalStatusPreference", "New marital status selected: " + newValue);
        }

        // Updates occupation.
        else if (preferenceKey.equals(ConfigViewModel.application.getString(R.string.occupation_preference_key))) {
            ConfigViewModel.databaseUserReference.child(ConfigViewModel.application.getString(R.string.occupation_db_label)).setValue(newValue);
            editor.putString(ConfigViewModel.application.getString(R.string.occupation_preference_key), newValue);
            Log.i("OccupationPreference", "New occupation selected: " + newValue);
        }

        editor.apply();
    }


    // Function that updates those user fields that require re-authentication for security reasons
    // (email and password).
    public void updateProfile(String itemWritten, String currentPassword, UpdateCredentialsAction action) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userEmail = ConfigViewModel.getUserPreferences().getString(ConfigViewModel.application.getString(
                R.string.email_preference_key), "");

        AuthCredential credential = EmailAuthProvider.getCredential(userEmail, currentPassword);
        if (user != null) {
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Log.d("Reauthentication", "User re-authenticated.");
                    // Decides which element of the user's profile to update
                    switch(action) {
                        case UPDATE_EMAIL:
                            user.updateEmail(itemWritten).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        String emailPrefKey = ConfigViewModel.application.getString(R.string.email_preference_key);
                                        ConfigViewModel.this.updateSelectedPreference(itemWritten, emailPrefKey);
                                        Log.d("UserEmail", "User's email successfully updated");
                                    }
                                }
                            });
                            break;

                        case UPDATE_PASSWORD:
                            user.updatePassword(itemWritten).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        String passwordPrefKey = ConfigViewModel.application.getString(R.string.password_preference_key);
                                        ConfigViewModel.this.updateSelectedPreference(itemWritten, passwordPrefKey);
                                        Log.d("UserPassword", "User's password successfully updated");
                                    }
                                }
                            });
                            break;

                        default:
                            break;
                    }
                }
            });
        }
    }


}