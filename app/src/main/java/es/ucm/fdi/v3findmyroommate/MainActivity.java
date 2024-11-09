package es.ucm.fdi.v3findmyroommate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private DatabaseReference databaseUserReference;
    private FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String userEmail = MainActivity.this.emailEditText.getText().toString();
                String userPassword = MainActivity.this.passwordEditText.getText().toString();

                // Accesses database and searches for the user's email.
                mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If the sign in is successful, updates preferences signed-in user's information.
                            setInitialPreferences();
                            openLoginView(); // Goes to the next screen.
                            Log.d("SignUp", "Sign up successful");
                        }
                        else {
                            // If the sign in fails, displays a message to the user.
                            Toast signUpFailedToast = Toast.makeText(MainActivity.this,
                                    R.string.sign_up_failed_toast_text, Toast.LENGTH_SHORT);
                            signUpFailedToast.show();
                            Log.w("SignUp", "Sign up failed", task.getException());
                        }
                    }
                });
            }
        });

        TextView signUP = findViewById(R.id.signUP);
        signUP.setPaintFlags(signUP.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

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

    // Loads initial preferences.
    private void setInitialPreferences() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance(getApplication().
                    getApplicationContext().getString(R.string.database_url));
            this.databaseUserReference = databaseInstance.getReference("users")
                    .child(user.getUid());

            this.databaseUserReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();

                    String databaseEmail = user.getEmail();
                    String databaseUsername = user.getDisplayName();
                    String databaseAgeRange = snapshot.child("age_range").getValue(String.class);
                    String databaseGender = snapshot.child("gender").getValue(String.class);

                    // Stores the data in SharedPreferences.
                    SharedPreferences userPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = userPreferences.edit();
                    editor.clear();

                    editor.putString(getString(R.string.email_preference_key), databaseEmail);
                    editor.putString(getString(R.string.username_preference_key), databaseUsername);
                    editor.putString(getString(R.string.age_range_preference_key), databaseAgeRange);
                    editor.putString(getString(R.string.gender_preference_key), databaseGender);

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

}