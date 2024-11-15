package es.ucm.fdi.v3findmyroommate.ui.config;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.ucm.fdi.v3findmyroommate.R;

public class ConfigViewModel extends AndroidViewModel {

    private final SharedPreferences userPreferences;
    private final DatabaseReference databaseUserReference;

    public ConfigViewModel(Application application) {
        super(application);
        this.userPreferences = PreferenceManager.getDefaultSharedPreferences(application
                .getApplicationContext());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(getApplication().
                    getApplicationContext().getString(R.string.database_url));

            this.databaseUserReference = databaseInstance.getReference("users")
                    .child(user.getUid());
        }
        else {
            Log.e("UserPreferences", "Can't find current user");
            throw new NullPointerException("User found to be null");
        }
    }


    public void updateSelectedPreference(String newValue, String preferenceKey) {
        SharedPreferences.Editor editor = this.userPreferences.edit();

        // Updates email.
        if (preferenceKey.equals(getApplication().getString(R.string.email_preference_key))) {
            editor.putString(getApplication().getString(R.string.email_preference_key), newValue);
            Log.i("EmailPreference", "New email: " + newValue);
        }

        // Updates password.
        else if (preferenceKey.equals(getApplication().getString(R.string.password_preference_key))) {
            editor.putString(getApplication().getString(R.string.password_preference_key), newValue);
            Log.i("PasswordPreference", "New password: " + newValue);
        }

        // Updates username.
        else if (preferenceKey.equals(getApplication().getString(R.string.username_preference_key))) {
            editor.putString(getApplication().getString(R.string.username_preference_key), newValue);
            Log.i("UsernamePreference", "New username: " + newValue);
        }

        // Updates gender.
        else if (preferenceKey.equals(getApplication().getString(R.string.gender_preference_key))) {
            this.databaseUserReference.child(getApplication().getString(R.string.gender_db_label)).setValue(newValue);
            editor.putString(getApplication().getString(R.string.gender_preference_key), newValue);
            Log.i("GenderPreference", "New gender selected: " + newValue);
        }

        // Updates age range.
        else if (preferenceKey.equals(getApplication().getString(R.string.age_range_preference_key))) {
            this.databaseUserReference.child(getApplication().getString(R.string.age_range_db_label)).setValue(newValue);
            editor.putString(getApplication().getString(R.string.age_range_preference_key), newValue);
            Log.i("AgeRangePreference", "New age range selected: " + newValue);
        }

        // Updates marital status.
        else if (preferenceKey.equals(getApplication().getString(R.string.marital_status_preference_key))) {
            this.databaseUserReference.child(getApplication().getString(R.string.marital_status_db_label)).setValue(newValue);
            editor.putString(getApplication().getString(R.string.marital_status_preference_key), newValue);
            Log.i("MaritalStatusPreference", "New marital status selected: " + newValue);
        }

        // Updates occupation.
        else if (preferenceKey.equals(getApplication().getString(R.string.occupation_preference_key))) {
            this.databaseUserReference.child(getApplication().getString(R.string.occupation_db_label)).setValue(newValue);
            editor.putString(getApplication().getString(R.string.occupation_preference_key), newValue);
            Log.i("OccupationPreference", "New occupation selected: " + newValue);
        }

        editor.apply();
    }


}