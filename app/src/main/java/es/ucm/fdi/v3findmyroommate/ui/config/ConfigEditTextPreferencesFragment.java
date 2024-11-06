package es.ucm.fdi.v3findmyroommate.ui.config;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import es.ucm.fdi.v3findmyroommate.R;

public class ConfigEditTextPreferencesFragment extends PreferenceFragmentCompat {

    private ConfigViewModel preferencesViewModel;
    private EditTextPreference usernamePref, emailPref, passwordPref, descriptionPref;
    private ListPreference ageRangePreference, genderPreference;

    private static final String NULL_USERNAME_TOAST_TEXT = "El nombre de usuario no puede estar vacío";
    private static final String NULL_EMAIL_TOAST_TEXT = "El email no puede ser nulo";
    private static final String NULL_PASSWORD_TOAST_TEXT = "Debe tener una contraseña";
    private static final String INVALID_EMAIL_TOAST_TEXT = "La dirección de correo ha de ser válida";

    private static final CharSequence[] AGE_ENTRIES_ARRAY = {"18-25", "25-35", "35-45", ">45"};
    private static final CharSequence[] GENDER_ENTRIES_ARRAY = {"Male", "Female"};


    public ConfigEditTextPreferencesFragment(ConfigViewModel configViewModel) {
        this.preferencesViewModel = configViewModel;
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        linkPreferencesToCode();
        configurePreferences();

    }


    // Links XML layout preferences with their respective java objects.
    private void linkPreferencesToCode() {
        this.usernamePref = findPreference("username_preference");
        this.emailPref = findPreference("email_preference");
        this.passwordPref = findPreference("password_preference");
        this.descriptionPref = findPreference("description_preference");
        this.ageRangePreference = findPreference("age_range_preference");
        this.genderPreference = findPreference("gender_preference");
    }


    // Configures each preference, along with its changeListener.
    private void configurePreferences() {
        setUsernamePreference(this.usernamePref);
        setEmailPreference(this.emailPref);
        setPasswordPreference(this.passwordPref);
        setDescriptionPreference(this.descriptionPref);
        setAgeRangePreference(this.ageRangePreference);
        setGenderPreference(this.genderPreference);
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


    // Sets up age range preference change listeners.
    private void setAgeRangePreference(ListPreference ageRangePreference) {
        if (ageRangePreference != null) {
            ageRangePreference.setEntries(AGE_ENTRIES_ARRAY);
            ageRangePreference.setEntryValues(AGE_ENTRIES_ARRAY);

            ageRangePreference.setOnPreferenceChangeListener((preference, newAgeRangeValue) -> {
                String ageRangeSelected = (String) newAgeRangeValue;
                this.preferencesViewModel.updateAgeRange(ageRangeSelected);
                Log.i("AgeRangePreference", "New age range selected: " + ageRangeSelected);
                return true;
            });
        }
    }


    // Sets up gender preference change listeners.
    private void setGenderPreference(ListPreference genderPref) {
        if (genderPref != null) {
            genderPref.setEntries(GENDER_ENTRIES_ARRAY);
            genderPref.setEntryValues(GENDER_ENTRIES_ARRAY);

            genderPref.setOnPreferenceChangeListener((preference, newGenderValue) -> {
                String genderSelected = (String) newGenderValue;
                this.preferencesViewModel.updateGender(genderSelected);
                Log.i("GenderPreference", "New gender selected: " + genderSelected);
                return true;
            });
        }
    }


    // Configures the words in each button of the given reference (valid only for EditTextPreferences).
    private void setEditTextPreferenceButtonText(EditTextPreference currentPref) {
        currentPref.setPositiveButtonText("Aceptar");
        currentPref.setNegativeButtonText("Cancelar");
    }



}
