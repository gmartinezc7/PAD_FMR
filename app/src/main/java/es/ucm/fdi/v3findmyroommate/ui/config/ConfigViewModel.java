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

    private SharedPreferences userPreferences;
    private DatabaseReference databaseUserReference;

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


    public void updateUserEmail(String newEmail) {
        SharedPreferences.Editor editor = this.userPreferences.edit();
        editor.putString(getApplication().getString(R.string.email_preference_key), newEmail);
        editor.apply();
        Log.i("EmailPreference", "New email: " + newEmail);
    }


    public void updateUserPassword(String newPassword) {
        SharedPreferences.Editor editor = this.userPreferences.edit();
        editor.putString(getApplication().getString(R.string.password_preference_key), newPassword);
        editor.apply();
        Log.i("PasswordPreference", "New password: " + newPassword);
    }


    public void updateUserUsername(String newUsername) {
        SharedPreferences.Editor editor = this.userPreferences.edit();
        editor.putString(getApplication().getString(R.string.username_preference_key), newUsername);
        editor.apply();
        Log.i("UsernamePreference", "New username: " + newUsername);
    }


    public void updateUserDescription(String newDescription) {
        this.databaseUserReference.child("description").setValue(newDescription);
    }


    public void updateUserAgeRange(String newAgeRange) {
        this.databaseUserReference.child("age_range").setValue(newAgeRange);
        SharedPreferences.Editor editor = this.userPreferences.edit();
        editor.putString(getApplication().getString(R.string.age_range_preference_key), newAgeRange);
        editor.apply();
        Log.i("AgeRangePreference", "New age range selected: " + newAgeRange);
    }


    public void updateUserGender(String newGender) {
        this.databaseUserReference.child("gender").setValue(newGender);
        SharedPreferences.Editor editor = this.userPreferences.edit();
        editor.putString(getApplication().getString(R.string.gender_preference_key), newGender);
        editor.apply();
        Log.i("GenderPreference", "New gender selected: " + newGender);
    }


}