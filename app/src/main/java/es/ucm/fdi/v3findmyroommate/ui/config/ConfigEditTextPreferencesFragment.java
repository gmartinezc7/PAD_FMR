package es.ucm.fdi.v3findmyroommate.ui.config;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import es.ucm.fdi.v3findmyroommate.R;

public class ConfigEditTextPreferencesFragment extends PreferenceFragmentCompat {

    private String currentAuthenticationEmail;
    private final ConfigViewModel preferencesViewModel;
    private EditTextPreference usernamePreference, emailPreference, passwordPreference;
    private ListPreference ageRangePreference, genderPreference, maritalStatusPreference, occupationPreference;

    private final CharSequence[] gender_entries = new CharSequence[2];
    private final CharSequence[] age_entries = new CharSequence[4];
    private final CharSequence[] marital_status_entries = new CharSequence[4];
    private final CharSequence[] occupation_entries = new CharSequence[4];


    public ConfigEditTextPreferencesFragment(ConfigViewModel configViewModel) {
        this.preferencesViewModel = configViewModel;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Context currentContext = getContext();
        SharedPreferences userPreferences;
        if (currentContext != null) {
            userPreferences = PreferenceManager.getDefaultSharedPreferences(
                    getContext());

            this.currentAuthenticationEmail = userPreferences.getString(getString(R.string.email_preference_key), "");
            setPreferencesFromResource(R.xml.preferences, rootKey);
            setListPreferencesArrays();
            linkPreferencesToCode();
            configurePreferences();

        }
    }


    public void setListPreferencesArrays() {
        // Edit the gender entries array.
        this.gender_entries[0] = getString(R.string.male_label);
        this.gender_entries[1] = getString(R.string.female_label);

        // Edit the age entries array.
        this.age_entries[0] = getString(R.string.young_adult_age_label);
        this.age_entries[1] = getString(R.string.adult_age_label);
        this.age_entries[2] = getString(R.string.grown_up_age_label);
        this.age_entries[3] = getString(R.string.senior_age_label);

        // Edit the marital status entries array.
        this.marital_status_entries[0] = getString(R.string.single_status_label);
        this.marital_status_entries[1] = getString(R.string.relationship_status_label);
        this.marital_status_entries[2] = getString(R.string.married_status_label);
        this.marital_status_entries[3] = getString(R.string.prefer_not_say_status_label);

        // Edit the occupation entries array.
        this.occupation_entries[0] = getString(R.string.employed_status_label);
        this.occupation_entries[1] = getString(R.string.unemployed_status_label);
        this.occupation_entries[2] = getString(R.string.student_status_label);
        this.occupation_entries[3] = getString(R.string.retired_status_label);

    }


    // Links XML layout preferences with their respective java objects.
    private void linkPreferencesToCode() {
        this.usernamePreference = findPreference(getString(R.string.username_preference_key));
        this.emailPreference = findPreference(getString(R.string.email_preference_key));
        this.passwordPreference = findPreference(getString(R.string.password_preference_key));
        this.ageRangePreference = findPreference(getString(R.string.age_range_preference_key));
        this.genderPreference = findPreference(getString(R.string.gender_preference_key));
        this.maritalStatusPreference = findPreference(getString(R.string.marital_status_preference_key));
        this.occupationPreference = findPreference(getString(R.string.occupation_preference_key));
    }


    // Configures each preference's changeListener.
    private void configurePreferences() {
        setEmailPreferenceListener(this.emailPreference);
        setPasswordPreferenceListener(this.passwordPreference);
        setUsernamePreferenceListener(this.usernamePreference);
        setAgeRangePreferenceListener(this.ageRangePreference);
        setGenderPreferenceListener(this.genderPreference);
        setMaritalStatusPreferenceListener(this.maritalStatusPreference);
        setOccupationPreferenceListener(this.occupationPreference);
    }


    // Sets up email preference change listener.
    private void setEmailPreferenceListener(EditTextPreference emailPreference) {
        if (emailPreference != null) {
            setEditTextPreferenceButtonText(emailPreference);
            emailPreference.setOnPreferenceChangeListener((preference, newEmailValue) -> {
                String emailWritten = (String) newEmailValue;
                if (emailWritten.isEmpty()) {
                    // Shows void email error toast.
                    Toast nullEmailToast = Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.null_email_toast_text),
                            Toast.LENGTH_SHORT);
                    nullEmailToast.show();
                    Log.e("emailPreference", "Email can't be void");
                    return false;
                }
                else if (!emailWritten.contains("@")) {
                    // Shows invalid email error toast.
                    Toast invalidEmailToast = Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.invalid_email_toast_text),
                            Toast.LENGTH_SHORT);
                    invalidEmailToast.show();
                    Log.e("emailPreference", "Email address must be a valid address");
                    return false;
                }
                else {
                    openAuthenticationDialog(emailWritten, UpdateCredentialsAction.UPDATE_EMAIL);
                    return true;
                }
            });
        }
    }


    // Sets up password preference change listener.
    private void setPasswordPreferenceListener(EditTextPreference passwordPreference) {
        if (passwordPreference != null) {
            setEditTextPreferenceButtonText(passwordPreference);
            passwordPreference.setOnPreferenceChangeListener((preference, newPasswordValue) -> {
                String passwordWritten = (String) newPasswordValue;
                if (passwordWritten.isEmpty()) {
                    // Shows void password error toast.
                    Toast nullPasswordToast = Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.null_password_toast_text),
                            Toast.LENGTH_SHORT);
                    nullPasswordToast.show();
                    Log.e("passwordPreference", "Password can't be void");
                    return false;
                }
                else {
                    openAuthenticationDialog(passwordWritten, UpdateCredentialsAction.UPDATE_PASSWORD);
                    return true;
                }
            });
        }
    }


    // Sets up user preference change listener.
    private void setUsernamePreferenceListener(EditTextPreference usernamePreference) {
        if (usernamePreference != null) {
            setEditTextPreferenceButtonText(usernamePreference);
            usernamePreference.setOnPreferenceChangeListener((preference, newUsernameValue) -> {
                String usernameWritten = (String) newUsernameValue;
                if (usernameWritten.isEmpty()) {
                    // Shows void username error toast.
                    Toast nullUsernameToast = Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.null_username_toast_text),
                            Toast.LENGTH_SHORT);
                    nullUsernameToast.show();
                    Log.e("usernamePreference", "Username can't be void");
                    return false;
                }
                else {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(usernameWritten).build();

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        user.updateProfile(profileUpdates).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    ConfigEditTextPreferencesFragment.this.preferencesViewModel
                                            .updateSelectedPreference(usernameWritten,
                                            getString(R.string.username_preference_key));
                                    Log.d("UserUsername", "User's username successfully updated");
                                }
                            }
                        });
                    }
                    else {
                        Log.e("UserPreferences", "Can't find current user");
                        throw new NullPointerException("User found to be null");
                    }
                    return true;
                }
            });
        }
    }


    // Sets up gender preference change listener.
    private void setGenderPreferenceListener(ListPreference genderPreference) {
        if (genderPreference != null) {
            genderPreference.setEntries(this.gender_entries);
            genderPreference.setEntryValues(this.gender_entries);
            genderPreference.setOnPreferenceChangeListener((preference, newGenderValue) -> {
                String genderSelected = (String) newGenderValue;
                this.preferencesViewModel.updateSelectedPreference(genderSelected, getString(
                        R.string.gender_preference_key));
                return true;
            });
        }
    }


    // Sets up age range preference change listener.
    private void setAgeRangePreferenceListener(ListPreference ageRangePreference) {
        if (ageRangePreference != null) {
            ageRangePreference.setEntries(this.age_entries);
            ageRangePreference.setEntryValues(this.age_entries);
            ageRangePreference.setOnPreferenceChangeListener((preference, newAgeRangeValue) -> {
                String ageRangeSelected = (String) newAgeRangeValue;
                this.preferencesViewModel.updateSelectedPreference(ageRangeSelected, getString(
                        R.string.age_range_preference_key));
                return true;
            });
        }
    }


    // Sets up marital status preference change listener.
    private void setMaritalStatusPreferenceListener(ListPreference maritalStatusPreference) {
        if (maritalStatusPreference != null) {
            maritalStatusPreference.setEntries(this.marital_status_entries);
            maritalStatusPreference.setEntryValues(this.marital_status_entries);
            maritalStatusPreference.setOnPreferenceChangeListener((preference, newMaritalStatusValue) -> {
                String maritalStatusSelected = (String) newMaritalStatusValue;
                this.preferencesViewModel.updateSelectedPreference(maritalStatusSelected, getString(
                        R.string.marital_status_preference_key));
                return true;
            });
        }
    }


    // Sets up occupation preference change listener.
    private void setOccupationPreferenceListener(ListPreference occupationPreference) {
        if (occupationPreference != null) {
            occupationPreference.setEntries(this.occupation_entries);
            occupationPreference.setEntryValues(this.occupation_entries);
            occupationPreference.setOnPreferenceChangeListener((preference, newOccupationValue) -> {
                String occupationSelected = (String) newOccupationValue;
                this.preferencesViewModel.updateSelectedPreference(occupationSelected, getString(
                        R.string.occupation_preference_key));
                return true;
            });
        }
    }


    // Method that displays the authentication dialog and updates the particular element of the profile
    // that was selected.
    private void openAuthenticationDialog(String itemWritten, UpdateCredentialsAction action) {
        // Inflates the custom layout.
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.reauthentication_dialog, null);

        // Finds the views inside the custom layout.
        EditText passwordEditText = customView.findViewById(R.id.reauth_dialog_password_edittext);
        Button submitButton = customView.findViewById(R.id.reauth_dialog_button);

        // Creates a dialog with the custom view.
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(customView);

        builder.setNegativeButton(R.string.cancel_text, (dialog, which) -> dialog.dismiss());

        // Creates and show the dialog.
        AlertDialog dialog = builder.create();

        // Sets an action for the submit button.
        submitButton.setOnClickListener(v -> {

            String currentPassword = passwordEditText.getText().toString();

            if (ConfigEditTextPreferencesFragment.this.currentAuthenticationEmail != null &&
                    !currentPassword.isEmpty()) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                AuthCredential credential = EmailAuthProvider.getCredential(
                        ConfigEditTextPreferencesFragment.this.currentAuthenticationEmail, currentPassword);
                if (user != null) {
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Log.d("Reauthentication", "User re-authenticated.");
                            // Decides which element of the user's profile to update
                            switch(action) {
                                case UPDATE_EMAIL:
                                    user.updateEmail(itemWritten).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                ConfigEditTextPreferencesFragment.this.preferencesViewModel
                                                        .updateSelectedPreference(itemWritten,
                                                        getString(R.string.email_preference_key));
                                                Log.d("UserEmail", "User's email successfully updated");
                                            }
                                        }
                                    });
                                    break;

                                case UPDATE_PASSWORD:
                                    user.updatePassword(itemWritten).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                ConfigEditTextPreferencesFragment.this.preferencesViewModel
                                                        .updateSelectedPreference(itemWritten,
                                                        getString(R.string.password_preference_key));
                                                Log.d("UserPassword", "User's password successfully updated");
                                            }
                                        }
                                    });
                                    break;

                                default:
                                    break;
                            }
                        }
                    });
                }
                dialog.dismiss(); // Closes the dialog.
            }

            else {
                // Shows a message if fields are empty.
                Toast fill_all_fields_toast = Toast.makeText(getContext(), R.string.fill_all_fields_toast_text,
                        Toast.LENGTH_SHORT);
                fill_all_fields_toast.show();
            }
        });

        // Shows the dialog.
        dialog.show();
    }


    // Configures the words in each button of the given reference (valid only for EditTextPreference).
    private void setEditTextPreferenceButtonText(EditTextPreference currentPref) {
        currentPref.setPositiveButtonText(R.string.accept_text);
        currentPref.setNegativeButtonText(R.string.cancel_text);
    }



}
