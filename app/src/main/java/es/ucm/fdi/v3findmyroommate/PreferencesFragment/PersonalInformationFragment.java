package es.ucm.fdi.v3findmyroommate.PreferencesFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.SharedViewModel;
import es.ucm.fdi.v3findmyroommate.User;
import es.ucm.fdi.v3findmyroommate.ui.config.ConfigPreferencesModel;

public class PersonalInformationFragment extends BaseFragment {
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout del fragmento
        View view = inflater.inflate(R.layout.personal_information_fragment, container, false);

        // Inicializa el ViewModel compartido
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Configura cada ChipGroup con su respectiva lógica
        setupChipGroup(view, R.id.genderChipGroup, (selectedValue) -> sharedViewModel.setGender(selectedValue));
        setupChipGroup(view, R.id.ageChipGroup, (selectedValue) -> sharedViewModel.setAgeRange(selectedValue));
        setupChipGroup(view, R.id.maritalStatusChipGroup, (selectedValue) -> sharedViewModel.setMaritalStatus(selectedValue));
        setupChipGroup(view, R.id.occupationChipGroup, (selectedValue) -> sharedViewModel.setOccupation(selectedValue));

        // Configura el botón Continuar
        Button continueButton = view.findViewById(R.id.continueButton);

        continueButton.setOnClickListener(v -> {
            // Recopilar ChipGroups a validar
            List<ChipGroup> requiredChipGroups = Arrays.asList(
                    view.findViewById(R.id.genderChipGroup),
                    view.findViewById(R.id.ageChipGroup),
                    view.findViewById(R.id.maritalStatusChipGroup),
                    view.findViewById(R.id.occupationChipGroup)
            );

            // Validar antes de continuar
            if (validateSelections(requiredChipGroups, null)) {
                updateUserInfoInDatabase(); // Calls the function that updates the user's info.
                loadNextFragment(getNextFragment()); // Continuar si la validación es exitosa
            }
        });

        return view;

    }


    @Override
    protected Fragment getNextFragment() {
        // Devuelve el siguiente fragmento que deseas cargar
        return new PropertyTypeFragment();
    }


    // Method that updates the user's personal info and its preferences in the database.
    protected void updateUserInfoInDatabase() {
        User userObject = sharedViewModel.getUser().getValue(); // Get user object to obtain its info.

        if (userObject != null) {
            // Get the values for each of the user object's data fields.
            String gender = sharedViewModel.getUser().getValue().getGender();
            String ageRange = sharedViewModel.getUser().getValue().getRangeAge();
            String maritalStatus = sharedViewModel.getUser().getValue().getMaritalStatus();
            String occupation = sharedViewModel.getUser().getValue().getOccupation();

            Activity currentActivity = getActivity();
            if (currentActivity != null) {
                // Uses ConfigPreferencesModel so that it updates both the shared preferences and the database values.
                ConfigPreferencesModel.updateSelectedPreference(gender, getString(R.string.gender_preference_key),
                        currentActivity.getApplication());
                ConfigPreferencesModel.updateSelectedPreference(ageRange, getString(R.string.age_range_preference_key),
                        currentActivity.getApplication());
                ConfigPreferencesModel.updateSelectedPreference(maritalStatus, getString(R.string.marital_status_preference_key),
                        currentActivity.getApplication());
                ConfigPreferencesModel.updateSelectedPreference(occupation, getString(R.string.occupation_preference_key),
                        currentActivity.getApplication());
            }
        }
    }

}
