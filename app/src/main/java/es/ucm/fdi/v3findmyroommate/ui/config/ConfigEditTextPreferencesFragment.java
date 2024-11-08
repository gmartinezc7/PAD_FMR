package es.ucm.fdi.v3findmyroommate.ui.config;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import es.ucm.fdi.v3findmyroommate.R;

public class ConfigEditTextPreferencesFragment extends PreferenceFragmentCompat {

    private ConfigViewModel preferencesViewModel;
    private EditTextPreference usernamePref, emailPref, passwordPref, descriptionPref;
    private ListPreference ageRangePreference, genderPreference;

    private static final CharSequence[] AGE_ENTRIES_ARRAY = {"18-25", "25-35", "35-45", ">45"};
    private static final CharSequence[] GENDER_ENTRIES_ARRAY = {"Male", "Female"};


    public ConfigEditTextPreferencesFragment(ConfigViewModel configViewModel) {
        this.preferencesViewModel = configViewModel;
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        SharedPreferences userPreferences = PreferenceManager.getDefaultSharedPreferences(
                getContext());
        SharedPreferences.Editor editor = userPreferences.edit();
        String username = userPreferences.getString(getString(R.string.username_preference_key),
                "default_value");
        String email = userPreferences.getString(getString(R.string.email_preference_key),
                "default_value");
        String password = userPreferences.getString(getString(R.string.password_preference_key),
                "default_value");
        String description = userPreferences.getString(getString(R.string.description_preference_key),
                "default_value");
        String ageRange = userPreferences.getString(getString(R.string.age_range_preference_key),
                "default_value");
        String gender = userPreferences.getString(getString(R.string.gender_preference_key),
                "default_value");

        setPreferencesFromResource(R.xml.preferences, rootKey);
        setDefaultValues(username, email, password, description, ageRange, gender);
        linkPreferencesToCode();
        configurePreferences();

    }


    // Links XML layout preferences with their respective java objects.
    private void linkPreferencesToCode() {
        this.usernamePref = findPreference(getString(R.string.username_preference_key));
        this.emailPref = findPreference(getString(R.string.email_preference_key));
        this.passwordPref = findPreference(getString(R.string.password_preference_key));
        this.descriptionPref = findPreference(getString(R.string.description_preference_key));
        this.ageRangePreference = findPreference(getString(R.string.age_range_preference_key));
        this.genderPreference = findPreference(getString(R.string.gender_preference_key));

        String ageRange = ageRangePreference.getValue();  // Get current value
        Log.d("PreferenceValue", "Current age range: " + ageRange);
    }


    // Configures each preference's changeListener.
    private void configurePreferences() {
        setUsernamePreferenceListener(this.usernamePref);
        setEmailPreferenceListener(this.emailPref);
        setPasswordPreferenceListener(this.passwordPref);
        setDescriptionPreferenceListener(this.descriptionPref);
        setAgeRangePreferenceListener(this.ageRangePreference);
        setGenderPreferenceListener(this.genderPreference);
    }


    // Sets the default values of the preferences.
    public void setDefaultValues(String username, String email, String password, String description,
                                 String ageRange, String gender) {

        SharedPreferences userPreferences = PreferenceManager.getDefaultSharedPreferences(
                getContext());
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.clear();

        editor.putString(getString(R.string.username_preference_key), username);
        editor.putString(getString(R.string.email_preference_key), email);
        editor.putString(getString(R.string.password_preference_key), password);
        editor.putString(getString(R.string.description_preference_key), description);
        editor.putString(getString(R.string.age_range_preference_key), ageRange);
        editor.putString(getString(R.string.gender_preference_key), gender);

        editor.apply();
    }


    // Sets up user preference change listeners.
    private void setUsernamePreferenceListener(EditTextPreference usernamePref) {
        if (usernamePref != null) {
            setEditTextPreferenceButtonText(usernamePref);
            usernamePref.setOnPreferenceChangeListener((preference, newUsernameValue) -> {
                String usernameWritten = (String) newUsernameValue;
                if (usernameWritten.isEmpty()) {
                    // Shows void username error toast.
                    Toast nullUsernameToast = Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.null_username_toast_text),
                            Toast.LENGTH_SHORT);
                    nullUsernameToast.show();
                    Log.e("UsernamePreference", "Username can't be void");
                    return false;
                } else {
                    this.preferencesViewModel.updateUsernameInDatabase(usernameWritten);
                    Log.i("UsernamePreference", "New username: " + usernameWritten);
                    return true;
                }
            });
        }

    }


    // Sets up email preference change listeners.
    private void setEmailPreferenceListener(EditTextPreference emailPref) {
        if (emailPref != null) {
            setEditTextPreferenceButtonText(emailPref);
            emailPref.setOnPreferenceChangeListener((preference, newEmailValue) -> {
                String emailWritten = (String) newEmailValue;
                if (emailWritten.isEmpty()) {
                    // Shows void email error toast.
                    Toast nullEmailToast = Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.null_email_toast_text),
                            Toast.LENGTH_SHORT);
                    nullEmailToast.show();
                    Log.e("EmailPreference", "Email can't be void");
                    return false;
                } else if (!emailWritten.contains("@")) {
                    // Shows invalid email error toast.
                    Toast invalidEmailToast = Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.invalid_email_toast_text),
                            Toast.LENGTH_SHORT);
                    invalidEmailToast.show();
                    Log.e("EmailPreference", "Email address must be a valid address");
                    return false;
                } else {
                    this.preferencesViewModel.updateEmailInDatabase(emailWritten);
                    Log.i("EmailPreference", "New email: " + emailWritten);
                    return true;
                }

            });
        }

    }


    // Sets up password preference change listeners.
    private void setPasswordPreferenceListener(EditTextPreference passwordPref) {
        if (passwordPref != null) {
            setEditTextPreferenceButtonText(passwordPref);
            passwordPref.setOnPreferenceChangeListener((preference, newPasswordValue) -> {
                String passwordWritten = (String) newPasswordValue;
                if (passwordWritten.isEmpty()) {
                    // Shows void password error toast.
                    Toast nullPasswordToast = Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.null_password_toast_text),
                            Toast.LENGTH_SHORT);
                    nullPasswordToast.show();
                    Log.e("PasswordPreference", "Password can't be void");
                    return false;
                } else {
                    this.preferencesViewModel.updatePasswordInDatabase(passwordWritten);
                    Log.i("PasswordPreference", "New password: " + passwordWritten);
                    return true;
                }
            });
        }
    }


    // Sets up description preference change listeners.
    private void setDescriptionPreferenceListener(EditTextPreference descriptionPref) {
        if (descriptionPref != null) {
            setEditTextPreferenceButtonText(descriptionPref);
            descriptionPref.setOnPreferenceChangeListener((preference, newDescriptionValue) -> {
                String descriptionWritten = (String) newDescriptionValue;
                this.preferencesViewModel.updateDescriptionInDatabase(descriptionWritten);
                Log.i("DescriptionPreference", "New description: " + descriptionWritten);
                return true;
            });
        }
    }


    // Sets up age range preference change listeners.
    private void setAgeRangePreferenceListener(ListPreference ageRangePreference) {
        if (ageRangePreference != null) {
            ageRangePreference.setEntries(AGE_ENTRIES_ARRAY);
            ageRangePreference.setEntryValues(AGE_ENTRIES_ARRAY);

            ageRangePreference.setOnPreferenceChangeListener((preference, newAgeRangeValue) -> {
                String ageRangeSelected = (String) newAgeRangeValue;
                this.preferencesViewModel.updateAgeRangeInDatabase(ageRangeSelected);
                Log.i("AgeRangePreference", "New age range selected: " + ageRangeSelected);
                return true;
            });
        }
    }


    // Sets up gender preference change listeners.
    private void setGenderPreferenceListener(ListPreference genderPref) {
        if (genderPref != null) {
            genderPref.setEntries(GENDER_ENTRIES_ARRAY);
            genderPref.setEntryValues(GENDER_ENTRIES_ARRAY);

            genderPref.setOnPreferenceChangeListener((preference, newGenderValue) -> {
                String genderSelected = (String) newGenderValue;
                this.preferencesViewModel.updateGenderInDatabase(genderSelected);
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
