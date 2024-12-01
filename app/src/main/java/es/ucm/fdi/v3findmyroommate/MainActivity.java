package es.ucm.fdi.v3findmyroommate;

import android.content.Intent;
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

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.Locale;

import es.ucm.fdi.v3findmyroommate.ui.config.ConfigPreferencesModel;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
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

        // Adds both locales to the locale class' locale list.
        LocaleUtils.addLocale("es");
        LocaleUtils.addLocale("en_US");

        // Sets the spanish locale as the default locale.
        //LocaleUtils.setDefaultLocale(this, "es");

        Locale[] availableLocales = Locale.getAvailableLocales();
        for (Locale locale : availableLocales) {
            Log.d("Locales", "Locale: " + locale.toString());
        }



        // TEST TEST TEST
        String mess = "Welkcome back!";
        if (LocaleUtils.doesStringMatchAnyLanguage(this, mess, R.string.welcome_message)) {
            Log.d("buttonName", "MATCH");
        }
        else {
            Log.d("buttonName", "NO MATCH");
        }
        // END OF TEST


        loginButton.setOnClickListener(view -> {
            String userEmail = MainActivity.this.emailEditText.getText().toString();
            String userPassword = MainActivity.this.passwordEditText.getText().toString();

            // If the user has entered all the fields.
            if (!userEmail.isEmpty() && !userPassword.isEmpty()) {

                // Accesses database and searches for the user's email.
                mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(MainActivity.this, task -> {
                        if (task.isSuccessful()) {
                            // If the sign in is successful, updates preferences signed-in user's information.
                            ConfigPreferencesModel.setInitialPreferences(this.getApplication());
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
            }

            else {
                Toast fillAlFieldsToast = Toast.makeText(MainActivity.this,
                        R.string.fill_all_fields_toast_text, Toast.LENGTH_SHORT);
                fillAlFieldsToast.show();
            }

        });

        TextView signUP = findViewById(R.id.signUP);
        signUP.setOnClickListener(view -> openSignUPView());
        signUP.setPaintFlags(signUP.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        signUP.setOnClickListener(view -> openSignUPView());

        signUP.setOnClickListener(view -> openSignUPView());

        //Notificaciones
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        //Guardar token del usuario
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                        userRef.child("fcmToken").setValue(token);
                    }
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

    private void sendTokenToBackend(String token) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //TODO
    }


}