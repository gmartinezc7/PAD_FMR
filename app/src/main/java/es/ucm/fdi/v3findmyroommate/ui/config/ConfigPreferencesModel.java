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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.ucm.fdi.v3findmyroommate.R;

public class ConfigPreferencesModel extends AndroidViewModel {

    private static SharedPreferences userPreferences;
    private static DatabaseReference databaseUserReference;

    public ConfigPreferencesModel(Application application) {
        super(application);
        ConfigPreferencesModel.getUserPreferences(application);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(application.
                    getApplicationContext().getString(R.string.database_url));

            ConfigPreferencesModel.databaseUserReference = databaseInstance.getReference("users")
                    .child(user.getUid());
        }
        else {
            Log.e("UserPreferences", "Can't find current user");
            throw new NullPointerException("User wasn't found");
        }

    }


    // Singleton method to retrieve the SharedPreferences.
    public static SharedPreferences getUserPreferences(Application application) {
        if (ConfigPreferencesModel.userPreferences == null)
            ConfigPreferencesModel.userPreferences = PreferenceManager.getDefaultSharedPreferences(
                    application.getApplicationContext());
        return ConfigPreferencesModel.userPreferences;
    }


    // Loads initial preferences.
    public static void setInitialPreferences(Application application) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(application.
                    getApplicationContext().getString(R.string.database_url));
            ConfigPreferencesModel.databaseUserReference = databaseInstance.getReference("users")
                    .child(user.getUid());

            ConfigPreferencesModel.databaseUserReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    String databaseEmail = user.getEmail();
                    String databaseUsername = user.getDisplayName();
                    String databaseAgeRange = snapshot.child(application
                            .getString(R.string.age_range_db_label)).getValue(String.class);
                    String databaseGender = snapshot.child(application
                            .getString(R.string.gender_db_label)).getValue(String.class);
                    String databaseMaritalStatus = snapshot.child(application
                            .getString(R.string.marital_status_db_label)).getValue(String.class);
                    String databaseOccupation = snapshot.child(application
                            .getString(R.string.occupation_db_label)).getValue(String.class);

                    // Stores the data in SharedPreferences.
                    SharedPreferences.Editor editor = ConfigPreferencesModel.getUserPreferences(application).edit();
                    editor.clear();

                    editor.putString(application.getString(R.string.email_preference_key),
                            databaseEmail);
                    editor.putString(application.getString(R.string.username_preference_key),
                            databaseUsername);
                    editor.putString(application.getString(R.string.age_range_preference_key),
                            databaseAgeRange);
                    editor.putString(application.getString(R.string.gender_preference_key),
                            databaseGender);
                    editor.putString(application.getString(R.string.marital_status_preference_key),
                            databaseMaritalStatus);
                    editor.putString(application.getString(R.string.occupation_preference_key),
                            databaseOccupation);

                    editor.apply();
                }
                else {
                    Log.e("Firebase", "Failed to retrieve user data", task.getException());
                }
            });
        }
        else {
            Log.e("UserPreferences", "Can't find current user");
            throw new NullPointerException("User found to be null");
        }
    }


    // Static method to update the selected preference.
    public static void updateSelectedPreference(String newValue, String preferenceKey, Application application) {
        SharedPreferences.Editor editor = ConfigPreferencesModel.getUserPreferences(application).edit();

        // Updates user's email.
        if (preferenceKey.equals(application.getString(R.string.email_preference_key))) {
            editor.putString(application.getString(R.string.email_preference_key), newValue);
            Log.i("EmailPreference", "New email: " + newValue);
        }

        // Updates user's password.
        else if (preferenceKey.equals(application.getString(R.string.password_preference_key))) {
            editor.putString(application.getString(R.string.password_preference_key), newValue);
            Log.i("PasswordPreference", "New password: " + newValue);
        }

        // Updates user's username.
        else if (preferenceKey.equals(application.getString(R.string.username_preference_key))) {
            editor.putString(application.getString(R.string.username_preference_key), newValue);
            Log.i("UsernamePreference", "New username: " + newValue);
        }

        // Updates user's gender.
        else if (preferenceKey.equals(application.getString(R.string.gender_preference_key))) {
            ConfigPreferencesModel.databaseUserReference.child(
                    application.getString(R.string.gender_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.gender_preference_key), newValue);
            Log.i("GenderPreference", "New gender selected: " + newValue);
        }

        // Updates user's age range.
        else if (preferenceKey.equals(application.getString(R.string.age_range_preference_key))) {
            ConfigPreferencesModel.databaseUserReference.child(
                    application.getString(R.string.age_range_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.age_range_preference_key), newValue);
            Log.i("AgeRangePreference", "New age range selected: " + newValue);
        }

        // Updates user's marital status.
        else if (preferenceKey.equals(application.getString(R.string.marital_status_preference_key))) {
            ConfigPreferencesModel.databaseUserReference.child(
                    application.getString(R.string.marital_status_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.marital_status_preference_key), newValue);
            Log.i("MaritalStatusPreference", "New marital status selected: " + newValue);
        }

        // Updates user's occupation.
        else if (preferenceKey.equals(application.getString(R.string.occupation_preference_key))) {
            ConfigPreferencesModel.databaseUserReference.child(
                    application.getString(R.string.occupation_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.occupation_preference_key), newValue);
            Log.i("OccupationPreference", "New occupation selected: " + newValue);
        }

        // Updates user's property type preference.
        else if (preferenceKey.equals(application.getString(R.string.property_type_preference_key))) {
            ConfigPreferencesModel.databaseUserReference.child(
                    application.getString(R.string.property_type_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.property_type_preference_key), newValue);
            Log.i("PropertyTypePreference", "New property type selected: " + newValue);
        }

        // Updates user's max budget.
        else if (preferenceKey.equals(application.getString(R.string.max_budget_preference_key))) {
            ConfigPreferencesModel.databaseUserReference.child(
                    application.getString(R.string.max_budget_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.max_budget_preference_key), newValue);
            Log.i("MaxBudgetPreference", "New max budget: " + newValue);
        }

        // Updates user's number of rooms preference.
        else if (preferenceKey.equals(application.getString(R.string.num_rooms_preference_key))) {
            ConfigPreferencesModel.databaseUserReference.child(
                    application.getString(R.string.num_rooms_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.num_rooms_preference_key), newValue);
            Log.i("NumberOfRoomsPreference", "New number of rooms selected: " + newValue);
        }

        // Updates user's number of bathrooms preference.
        else if (preferenceKey.equals(application.getString(R.string.num_bathrooms_preference_key))) {
            ConfigPreferencesModel.databaseUserReference.child(
                    application.getString(R.string.num_rooms_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.num_bathrooms_preference_key), newValue);
            Log.i("NumberOfBathroomsPreference", "New number of bathrooms selected: " + newValue);
        }

        // Updates user's orientation preference.
        else if (preferenceKey.equals(application.getString(R.string.orientation_preference_key))) {
            ConfigPreferencesModel.databaseUserReference.child(
                    application.getString(R.string.orientation_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.orientation_preference_key), newValue);
            Log.i("OrientationPreference", "New orientation selected: " + newValue);
        }

        // Updates user's square meters preference.
        else if (preferenceKey.equals(application.getString(R.string.square_meters_preference_key))) {
            ConfigPreferencesModel.databaseUserReference.child(
                    application.getString(R.string.square_meters_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.square_meters_preference_key), newValue);
            Log.i("SquareMatersPreference", "New square meters amount: " + newValue);
        }

        // Updates user's max number of roommates preference.
        else if (preferenceKey.equals(application.getString(R.string.max_num_roommates_preference_key))) {
            ConfigPreferencesModel.databaseUserReference.child(
                    application.getString(R.string.max_num_roommates_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.max_num_roommates_preference_key), newValue);
            Log.i("MaxNumberOfRoommatesPreference", "New maximum number of roommates selected: " + newValue);
        }

        // Updates user's roommates' gender preferences.
        else if (preferenceKey.equals(application.getString(R.string.roommate_gender_preference_key))) {
            ConfigPreferencesModel.databaseUserReference.child(
                    application.getString(R.string.roommate_gender_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.roommate_gender_preference_key), newValue);
            Log.i("RoommateGenderPreference", "New roommates' gender selected: " + newValue);
        }

        // Updates user's bathroom type preference.
        else if (preferenceKey.equals(application.getString(R.string.bathroom_type_preference_key))) {
            ConfigPreferencesModel.databaseUserReference.child(
                    application.getString(R.string.bathroom_type_db_label)).setValue(newValue);
            editor.putString(application.getString(R.string.bathroom_type_preference_key), newValue);
            Log.i("BathroomTypePreference", "New bathroom type selected: " + newValue);
        }

        editor.apply();
    }


    // Method that updates those user fields that require re-authentication for security reasons
    // (email and password).
    public static void updateProfile(String itemWritten, String currentPassword, UpdateCredentialsAction action,
                                     Application application) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userEmail = ConfigPreferencesModel.getUserPreferences(application).getString(application.getString(
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
                                ConfigPreferencesModel.updateSelectedPreference(itemWritten, emailPrefKey, application);
                                Log.d("UserEmail", "User's email successfully updated");
                            }
                        });
                        break;

                    case UPDATE_PASSWORD:
                        user.updatePassword(itemWritten).addOnCompleteListener(task12 -> {
                            if (task12.isSuccessful()) {
                                String passwordPrefKey = application.getString(R.string.password_preference_key);
                                ConfigPreferencesModel.updateSelectedPreference(itemWritten, passwordPrefKey, application);
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