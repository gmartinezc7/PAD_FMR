package es.ucm.fdi.v3findmyroommate.ui.config;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.ucm.fdi.v3findmyroommate.R;

public class ConfigViewModel extends AndroidViewModel {

    private String userID;
    private final DatabaseReference databaseUserReference;;

    public ConfigViewModel(Application application) {
        super(application);
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(application.getResources().
                getString(R.string.database_url));

        SharedPreferences userPreferences = PreferenceManager.getDefaultSharedPreferences(application
                .getApplicationContext());
        this.userID = userPreferences.getString("user_id", "0");

        this.databaseUserReference = databaseInstance.getReference("users")
                .child(this.userID);
    }

    public void updateUsernameInDatabase(String newUsername) {
        this.databaseUserReference.child("username").setValue(newUsername);
    }

    public void updateEmailInDatabase(String newEmail) {
        this.databaseUserReference.child("email").setValue(newEmail);
    }

    public void updatePasswordInDatabase(String newPassword) {
        this.databaseUserReference.child("password").setValue(newPassword);
    }

    public void updateDescriptionInDatabase(String newDescription) {
        this.databaseUserReference.child("description").setValue(newDescription);
    }

    public void updateAgeRangeInDatabase(String newAgeRange) {
        this.databaseUserReference.child("age_range").setValue(newAgeRange);
    }

    public void updateGenderInDatabase(String newGender) {
        this.databaseUserReference.child("gender").setValue(newGender);
    }


}