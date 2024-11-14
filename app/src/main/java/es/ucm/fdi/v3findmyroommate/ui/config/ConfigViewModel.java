package es.ucm.fdi.v3findmyroommate.ui.config;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import es.ucm.fdi.v3findmyroommate.R;

public class ConfigViewModel extends AndroidViewModel {

    private SharedPreferences userPreferences;

    public ConfigViewModel(Application application) {
        super(application);
        this.userPreferences = PreferenceManager.getDefaultSharedPreferences(application
                .getApplicationContext());


        //this.userID = userPreferences.getString("user_id", "0");

        //this.databaseUserReference = databaseInstance.getReference("users")
        //        .child(this.userID);
    }

    public void updateUsernameInDatabase(String newUsername) {
        //this.databaseUserReference.child("username").setValue(newUsername);
    }

    public void updateEmailPreference(String newEmail) {
        SharedPreferences.Editor editor = this.userPreferences.edit();
        editor.putString(getApplication().getString(R.string.email_preference_key), newEmail);
        editor.apply();
        Log.i("EmailPreference", "New email: " + newEmail);
    }

    public void updatePasswordPreference(String newPassword) {
        SharedPreferences.Editor editor = this.userPreferences.edit();
        editor.putString(getApplication().getString(R.string.password_preference_key), newPassword);
        editor.apply();
        Log.i("PasswordPreference", "New password: " + newPassword);
    }

    public void updateDescriptionInDatabase(String newDescription) {
        //this.databaseUserReference.child("description").setValue(newDescription);
    }

    public void updateAgeRangeInDatabase(String newAgeRange) {
        //this.databaseUserReference.child("age_range").setValue(newAgeRange);
    }

    public void updateGenderInDatabase(String newGender) {
        //this.databaseUserReference.child("gender").setValue(newGender);
    }


}