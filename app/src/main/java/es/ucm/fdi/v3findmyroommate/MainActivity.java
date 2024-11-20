package es.ucm.fdi.v3findmyroommate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.ucm.fdi.v3findmyroommate.ui.config.ConfigPreferencesModel;

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
        loginButton.setOnClickListener(view -> {
            String userEmail = MainActivity.this.emailEditText.getText().toString();
            String userPassword = MainActivity.this.passwordEditText.getText().toString();

            // Accesses database and searches for the user's email.
            mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(MainActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // If the sign in is successful, updates preferences signed-in user's information.
                        ConfigPreferencesModel.setInitialPreferences(this.getApplication());
//                        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                        //NavigationUI.setupActionBarWithNavController(this, navController);
                        openLoginView(); // Goes to the next screen.
                        Log.d("SignIn", "Sign in successful");
                    }
                    else {
                        // If the sign in fails, displays a message to the user.
                        Toast signInFailedToast = Toast.makeText(MainActivity.this,
                                R.string.sign_in_failed_toast_text, Toast.LENGTH_SHORT);
                        signInFailedToast.show();
                        Log.w("SignIn", "Sign in failed", task.getException());
                    }
                });
        });

        TextView signUP = findViewById(R.id.signUP);
        signUP.setPaintFlags(signUP.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openSignUPView();}
        });

        signUP.setOnClickListener(view -> openSignUPView());
    }

    public void openLoginView(){
        Intent intent = new Intent(MainActivity.this, Lobby.class);
        startActivity(intent);
    }


    public void openSignUPView(){
        Intent intent = new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);
    }

/*
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

 */


}