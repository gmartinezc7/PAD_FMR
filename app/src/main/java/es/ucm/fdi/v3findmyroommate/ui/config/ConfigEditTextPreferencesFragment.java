package es.ucm.fdi.v3findmyroommate.ui.config;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import es.ucm.fdi.v3findmyroommate.R;

public class ConfigEditTextPreferencesFragment extends PreferenceFragmentCompat {

    private ConfigViewModel preferencesViewModel;

    private static final String NULL_USERNAME_TOAST_TEXT = "El nombre de usuario no puede estar vacío";
    private static final String NULL_EMAIL_TOAST_TEXT = "El email no puede ser nulo";
    private static final String NULL_PASSWORD_TOAST_TEXT = "Debe tener una contraseña";
    private static final String INVALID_EMAIL_TOAST_TEXT = "La dirección de correo ha de ser válida";


    public ConfigEditTextPreferencesFragment(ConfigViewModel configViewModel) {
        this.preferencesViewModel = configViewModel;
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Gets references to the preferences.
        EditTextPreference usernamePref = findPreference("username_preference");
        EditTextPreference emailPref = findPreference("email_preference");
        EditTextPreference passwordPref = findPreference("password_preference");
        EditTextPreference descriptionPref = findPreference("description_preference");

        setUsernamePreference(usernamePref);
        setEmailPreference(emailPref);
        setPasswordPreference(passwordPref);
        setDescriptionPreference(descriptionPref);

    }


    // Sets up user preference change listeners.
    private void setUsernamePreference(EditTextPreference usernamePref) {
        if (usernamePref != null) {
            setEditTextPreferenceButtonText(usernamePref);
            usernamePref.setOnPreferenceChangeListener((preference, newUsernameValue) -> {
                String usernameWritten = (String) newUsernameValue;
                if (usernameWritten.isEmpty()) {
                    // Shows void username error toast.
                    Toast nullUsernameToast = Toast.makeText(getActivity(), NULL_USERNAME_TOAST_TEXT,
                            Toast.LENGTH_SHORT);
                    nullUsernameToast.show();
                    Log.e("UsernamePreference", "Username can't be void");
                    return false;
                }

                else {
                    this.preferencesViewModel.updateUsername(usernameWritten);
                    Log.i("UsernamePreference", "New username: " + usernameWritten);
                    return true;
                }
            });
        }

    }


    // Sets up email preference change listeners.
    private void setEmailPreference(EditTextPreference emailPref) {
        if (emailPref != null) {
            setEditTextPreferenceButtonText(emailPref);
            emailPref.setOnPreferenceChangeListener((preference, newEmailValue) -> {
                String emailWritten = (String) newEmailValue;
                if (emailWritten.isEmpty()) {
                    // Shows void email error toast.
                    Toast nullEmailToast = Toast.makeText(getActivity(), NULL_EMAIL_TOAST_TEXT,
                            Toast.LENGTH_SHORT);
                    nullEmailToast.show();
                    Log.e("EmailPreference", "Email can't be void");
                    return false;
                }

                else if (!emailWritten.contains("@")) {
                    // Shows invalid email error toast.
                    Toast invalidEmailToast = Toast.makeText(getActivity(), INVALID_EMAIL_TOAST_TEXT,
                            Toast.LENGTH_SHORT);
                    invalidEmailToast.show();
                    Log.e("EmailPreference", "Email address must be a valid address");
                    return false;
                }

                else {
                    this.preferencesViewModel.updateEmail(emailWritten);
                    Log.i("EmailPreference", "New email: " + emailWritten);
                    return true;
                }

            });
        }

    }


    // Sets up password preference change listeners.
    private void setPasswordPreference(EditTextPreference passwordPref) {
        if (passwordPref != null) {
            setEditTextPreferenceButtonText(passwordPref);
            passwordPref.setOnPreferenceChangeListener((preference, newPasswordValue) -> {
                String passwordWritten = (String) newPasswordValue;
                if (passwordWritten.isEmpty()) {
                    // Shows void password error toast.
                    Toast nullPasswordToast = Toast.makeText(getActivity(), NULL_PASSWORD_TOAST_TEXT,
                            Toast.LENGTH_SHORT);
                    nullPasswordToast.show();
                    Log.e("PasswordPreference", "Password can't be void");
                    return false;
                }

                else {
                    this.preferencesViewModel.updatePassword(passwordWritten);
                    Log.i("PasswordPreference", "New password: " + passwordWritten);
                    return true;
                }
            });
        }
    }


    // Sets up description preference change listeners.
    private void setDescriptionPreference(EditTextPreference descriptionPref) {
        if (descriptionPref != null) {
            setEditTextPreferenceButtonText(descriptionPref);
            descriptionPref.setOnPreferenceChangeListener((preference, newDescriptionValue) -> {
                String descriptionWritten = (String) newDescriptionValue;
                this.preferencesViewModel.updateDescription(descriptionWritten);
                Log.i("DescriptionPreference", "New description: " + descriptionWritten);
                return true;
            });
        }
    }


    // Configures the words in each button of the given reference.
    private void setEditTextPreferenceButtonText(EditTextPreference currentPref) {
        currentPref.setPositiveButtonText("Aceptar");
        currentPref.setNegativeButtonText("Cancelar");
    }



}
