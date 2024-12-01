package es.ucm.fdi.v3findmyroommate;

import static android.util.Log.e;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import es.ucm.fdi.v3findmyroommate.ui.config.ConfigPreferencesModel;

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

            requestPermissions();

        }
        catch (Exception e) {
            e(TAG, e.getMessage());
        }
    }


    private void abrirTrasIniciar(){

        // Obtener los datos de los campos de texto
        String name = userNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = userEmailEditText.getText().toString().trim();
        String password = userPasswordTextEdit.getText().toString().trim();
        String confirmPassword = userConfirmPasswordTextEdit.getText().toString().trim();

        // Verifies that all fields are filled.
        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            // Shows error toast if there are any empty fields.
            Toast.makeText(this, R.string.fill_all_fields_toast_text, Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifies that the email entered is a valid email address.
        else if (!email.contains("@")) {
            // Shows error toast if the email isn't valid.
            Toast.makeText(this, R.string.invalid_email_toast_text, Toast.LENGTH_SHORT).show();
        }

        // Verifies that both passwords entered match.
        else if (!password.equals(confirmPassword)) {
            // Shows error toast if the passwords don't match.
            Toast.makeText(this, R.string.passwords_dont_match_toast_text, Toast.LENGTH_SHORT).show();
            return;
        }

        else {
            // Start next activity if the database doesn't throw any errors
            Intent intent = new Intent(SignUp.this, PreferenceUser.class);
            intent.putExtra("userName", name);
            intent.putExtra("lastName", lastName);
            intent.putExtra("userEmail", email);
            intent.putExtra("userPassword", password);

            createUserInDatabase(name, email, lastName, password, intent);

        }


    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);

            } else {
                abrirTrasIniciar();
            }
        } else {
            abrirTrasIniciar();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Llamada a la implementación base
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirTrasIniciar();
            } else {
                Toast.makeText(this, this.getString(R.string.mensaje_permisos_requeridos_denegados), Toast.LENGTH_SHORT).show();
            }
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
                                    ConfigPreferencesModel.updateSelectedPreference(fullUsername,
                                            getString(R.string.username_preference_key), getApplication());
                                    if (task.isSuccessful()) {
                                        Log.d("UserUsername", "User username updated.");
                                    }
                                }
                            });
                        }
                        ConfigPreferencesModel cVM = new ConfigPreferencesModel(getApplication());
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