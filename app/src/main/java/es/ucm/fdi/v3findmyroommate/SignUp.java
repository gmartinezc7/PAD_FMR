package es.ucm.fdi.v3findmyroommate;

import static android.util.Log.e;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseAuthWebException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import es.ucm.fdi.v3findmyroommate.PreferencesFragment.PreferenceUser;
import es.ucm.fdi.v3findmyroommate.ui.config.ConfigViewModel;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp";
    private EditText userNameEditText;
    private EditText lastNameEditText;
    private EditText userEmailEditText;
    private EditText userPasswordTextEdit;
    private EditText userConfirmPasswordTextEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Obtén las referencias de los EditText
        userNameEditText = findViewById(R.id.userNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        userEmailEditText = findViewById(R.id.userEmailEditText);
        userPasswordTextEdit = findViewById(R.id.userPasswordTextEdit);
        userConfirmPasswordTextEdit = findViewById(R.id.confirmUserPasswordEditText);

        TextView txt = (TextView) findViewById(R.id.signInTextView);
        txt.setPaintFlags(txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }

    public void createAccount(View view) {
        try {
            // Obtener los datos de los campos de texto
            String name = userNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String email = userEmailEditText.getText().toString().trim();
            String password = userPasswordTextEdit.getText().toString().trim();
            String confirmPassword = userConfirmPasswordTextEdit.getText().toString().trim();

            // Verificar que todos los campos estén completos
            if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                // Mostrar mensaje de error si algún campo está vacío
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar que las contraseñas coincidan
            if (!password.equals(confirmPassword)) {
                // Mostrar mensaje de error si las contraseñas no coinciden
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }


            // Iniciar la siguiente actividad si la bd no da errores
            Intent intent = new Intent(SignUp.this, PreferenceUser.class);
            intent.putExtra("userName", name);
            intent.putExtra("lastName", lastName);
            intent.putExtra("userEmail", email);
            intent.putExtra("userPassword", password);

            createUserInDatabase(name, email, lastName, password, intent);
        }
        catch (Exception e) {
            e(TAG, e.getMessage());
        }
    }

    public void returnLogin(View view) {
        Intent intent = new Intent(SignUp.this, MainActivity.class);
        startActivity(intent);
    }


    private void createUserInDatabase(String name, String email, String lastName, String password,
                                      Intent intent) {
        // Authenticates and creates a new user in the Firebase project.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // If sign in success, goes to the next fragment.
                        Log.d("UserCreation", "User successfully created");

                        String fullUsername = name + lastName;

                        // Creates the request to change the user's username in the database.
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(fullUsername).build();
                        if (user != null) {
                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    ConfigViewModel.updateSelectedPreference(fullUsername,
                                            getString(R.string.username_preference_key), getApplication());
                                    if (task.isSuccessful()) {
                                        Log.d("UserUsername", "User username updated.");
                                    }
                                }
                            });
                        }
                        ConfigViewModel cVM = new ConfigViewModel(getApplication());
                        startActivity(intent);
                    }
                    else {
                        Toast failedUserCreationToast;
                        Log.w("UserCreation", "Error creating user: ", task.getException());
                        // Configures the toast text according to the exception thrown.
                        try {
                            throw task.getException();
                        }
                        catch (FirebaseAuthUserCollisionException fAUCE) {
                            failedUserCreationToast = Toast.makeText(SignUp.this,
                                    R.string.duplicated_user_text, Toast.LENGTH_SHORT);
                        }
                        catch (FirebaseAuthWeakPasswordException fAWPE) {
                            failedUserCreationToast = Toast.makeText(SignUp.this,
                                    R.string.weak_password_text, Toast.LENGTH_SHORT);
                        }
                        catch (FirebaseAuthWebException fAWE) {
                            failedUserCreationToast = Toast.makeText(SignUp.this,
                                    R.string.user_auth_web_problem_text, Toast.LENGTH_SHORT);
                        }
                        catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        failedUserCreationToast.show(); // Shows user the dialog.
                    }
                }
            });
    }

}