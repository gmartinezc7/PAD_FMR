package es.ucm.fdi.v3findmyroommate.ui.config;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigViewModel extends AndroidViewModel {

    private DatabaseReference databaseUserReference;
    private PreferenceInitializationRepository preferenceRepository;

    private String testUserID = "U-0001";
    private static final String FMR_FIREBASE_DATABASE_URL =
            "https://findmyroommate-86cbe-default-rtdb.europe-west1.firebasedatabase.app/";


    public ConfigViewModel(Application application) {
        super(application);
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(FMR_FIREBASE_DATABASE_URL);
        this.databaseUserReference = databaseInstance.getReference("users")
                .child(this.testUserID);

        // Creates the repository to load the initial configuration.
        this.preferenceRepository = new PreferenceInitializationRepository(application);
        loadInitialPreferencesFromDatabase();
    }

    private void loadInitialPreferencesFromDatabase() {
        // Using Task to retrieve data
        Task<DataSnapshot> task = this.databaseUserReference.get();

        task.addOnCompleteListener(taskResult -> {
            if (taskResult.isSuccessful()) {
                DataSnapshot dataSnapshot = taskResult.getResult();
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String password = dataSnapshot.child("password").getValue().toString();
                    String description = dataSnapshot.child("description").getValue().toString();
                    // Loads the user's preferences.
                    this.preferenceRepository.setDefaultValues(username, email, password, description);
                }
            }
            else {  // Errors in the database.
                Log.e("FirebaseDatabase", "Error loading initial preferences");
            }
        });
    }

    public void updateUsername(String newUsername) {
        this.databaseUserReference.child("username").setValue(newUsername);
    }

    public void updateEmail(String newEmail) {
        this.databaseUserReference.child("email").setValue(newEmail);
    }

    public void updatePassword(String newPassword) {
        this.databaseUserReference.child("password").setValue(newPassword);
    }

    public void updateDescription(String newDescription) {
        this.databaseUserReference.child("description").setValue(newDescription);
    }


}