package es.ucm.fdi.v3findmyroommate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private int currentTestId;

    private EditText emailEditText, passwordEditText;
    private FirebaseDatabase databaseInstance;
    private DatabaseReference databaseUserReference;

    private static final int MAX_USER_ID = 30;
    private static final int DEFAULT_USER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        this.emailEditText = findViewById(R.id.emailEditText);
        this.passwordEditText = findViewById(R.id.passwordEditText);


        FirebaseApp.initializeApp(this);
        this.databaseInstance = FirebaseDatabase.getInstance(this.getResources().
                getString(R.string.database_url));

        this.databaseUserReference = this.databaseInstance.getReference("users").
                child(String.valueOf(this.currentTestId));


        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Access database and searches for the user's email.
                MainActivity.this.checkLoginCredentials();
            }
        });

        TextView signUP = findViewById(R.id.signUP);
        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openSignUPView();}
        });
    }

    public void openLoginView(){
        Intent intent = new Intent(MainActivity.this, Lobby.class);
        startActivity(intent);

    }
    public void openSignUPView(){
        Intent intent = new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);

    }


    private void checkLoginCredentials() {
        // Starts the search with the default first user ID.
        this.currentTestId = DEFAULT_USER_ID;
        checkNextUser();
    }

    private void checkNextUser() {
        if (this.currentTestId > MAX_USER_ID) {
            // If all users have been checked, shows a toast message.
            Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
            return; // Stops further execution.
        }

        // Gets a reference to the user in the Firebase database.
        this.databaseUserReference = this.databaseInstance.getReference("users")
                .child(String.valueOf(this.currentTestId));

        // Performs the database query.
        Task<DataSnapshot> task = databaseUserReference.get();
        task.addOnCompleteListener(taskResult -> {
            if (taskResult.isSuccessful()) {
                DataSnapshot dataSnapshot = taskResult.getResult();
                if (dataSnapshot.exists()) {
                    // Gets user data from firebase database.
                    String databaseEmail = dataSnapshot.child("email").getValue(String.class);
                    String databasePassword = dataSnapshot.child("password").getValue(String.class);
                    String inputEmail = emailEditText.getText().toString();
                    String inputPassword = passwordEditText.getText().toString();

                    if (databaseEmail != null && databasePassword != null &&
                            databaseEmail.equals(inputEmail) && databasePassword.equals(inputPassword)) {
                        // User found, load preferences and navigate to the next screen
                        String databaseUsername = dataSnapshot.child("username").getValue(String.class);
                        String databaseDescription = dataSnapshot.child("description").getValue(String.class);
                        String databaseAgeRange = dataSnapshot.child("age_range").getValue(String.class);
                        String databaseGender = dataSnapshot.child("gender").getValue(String.class);

                        setInitialPreferences(databaseUsername, databaseEmail, databasePassword,
                                databaseDescription, databaseAgeRange, databaseGender);

                        openLoginView(); // Goes to the next screen.
                    }
                    else {
                        // If the user's credentials don't match, increments the ID and try next user.
                        this.currentTestId++;
                        checkNextUser();
                    }
                }
                else {
                    // If user doesn't exist, try the next user.
                    this.currentTestId++;
                    checkNextUser();
                }
            }
            else {
                // Handle errors with the Firebase query
                Log.e("FirebaseDatabase", "Error loading user data");
                this.currentTestId++;
                checkNextUser(); // Continue with the next user
            }
        });
    }


    // Loads initial preferences.
    private void setInitialPreferences(String databaseUsername, String databaseEmail, String databasePassword,
                                       String databaseDescription, String databaseAgeRange, String databaseGender) {

        SharedPreferences userPreferences = PreferenceManager.getDefaultSharedPreferences(
                MainActivity.this);
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.clear();

        editor.putString(getString(R.string.username_preference_key), databaseUsername);
        editor.putString(getString(R.string.email_preference_key), databaseEmail);
        editor.putString(getString(R.string.password_preference_key), databasePassword);
        editor.putString(getString(R.string.description_preference_key), databaseDescription);
        editor.putString(getString(R.string.age_range_preference_key), databaseAgeRange);
        editor.putString(getString(R.string.gender_preference_key), databaseGender);
        editor.putString("user_id", String.valueOf(this.currentTestId));

        editor.apply();
    }

}