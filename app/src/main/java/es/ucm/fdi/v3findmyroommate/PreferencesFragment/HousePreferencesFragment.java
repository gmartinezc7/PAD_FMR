package es.ucm.fdi.v3findmyroommate.PreferencesFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.Lobby;
import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.SharedViewModel;
import es.ucm.fdi.v3findmyroommate.User;
import es.ucm.fdi.v3findmyroommate.ui.config.ConfigPreferencesModel;

public class HousePreferencesFragment extends PropertyTypeFragment {

    private Button continueButton;
    private SharedViewModel sharedViewModel;
    private TextInputEditText squareMetersInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.house_preferences_fragment, container, false);

        // Initialize the shared ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Setup ChipGroups with a unified listener
        setupChipGroup(view, R.id.propertyTypeChipGroup, selectedText -> sharedViewModel.setPropertyType(selectedText));
        setupChipGroup(view, R.id.roomsChipGroup, selectedText -> sharedViewModel.setNumberOfRooms(selectedText));
        setupChipGroup(view, R.id.bathroomsChipGroup, selectedText -> sharedViewModel.setNumberOfBathrooms(selectedText));
        setupChipGroup(view, R.id.orientationChipGroup, selectedText -> sharedViewModel.setOrientation(selectedText));

        // Setup square meters input
        squareMetersInput = view.findViewById(R.id.squareMetersInput);


        // Setup Continue button
        continueButton = view.findViewById(R.id.continueButton);

        continueButton.setOnClickListener(v -> {
            // Recopila los ChipGroups y TextInputEditText a validar
            List<ChipGroup> requiredChipGroups = Arrays.asList(
                    view.findViewById(R.id.propertyTypeChipGroup),
                    view.findViewById(R.id.roomsChipGroup),
                    view.findViewById(R.id.bathroomsChipGroup),
                    view.findViewById(R.id.orientationChipGroup)
            );

            List<TextInputEditText> requiredTextInputs = Arrays.asList(squareMetersInput); // Aquí se puede agregar más si es necesario

            // Llama a validateSelections para verificar que los campos no estén vacíos
            if (validateSelections(requiredChipGroups, requiredTextInputs)) {
                String squareMeters = squareMetersInput.getText().toString();
                sharedViewModel.setSquareMeters(squareMeters);

                updateUserInfoInDatabase();

                // Creates an intent to start the new activity.
                Intent intent = new Intent(getActivity(), Lobby.class);
                startActivity(intent); // Redirects the user to the app lobby.
            }
        });

        return view;
    }


    @Override
    protected Fragment getNextFragment() {
        return null;    // Returns null since there are no more fragments to edit user's info.
    }


    // Method that updates the user's personal info and its preferences in the database.
    protected void updateUserInfoInDatabase() {
        User userObject = sharedViewModel.getUser().getValue(); // Get user object to obtain its info.

        if (userObject != null) {
            // Get the values for each of the user object's data fields.
            String propertyType = sharedViewModel.getUser().getValue().getPropertyType();
            String numberOfRooms = sharedViewModel.getUser().getValue().getRooms();
            String numberOfBathrooms = sharedViewModel.getUser().getValue().getBathrooms();
            String orientation = sharedViewModel.getUser().getValue().getOrientation();
            String squareMeters = sharedViewModel.getUser().getValue().getSquareMeters();

            Activity currentActivity = getActivity();
            if (currentActivity != null) {
                // Uses ConfigPreferencesModel so that it updates both the shared preferences and the database values.
                ConfigPreferencesModel.updateSelectedPreference(propertyType, getString(R.string.house_type_preference_key),
                        currentActivity.getApplication());
                ConfigPreferencesModel.updateSelectedPreference(numberOfRooms, getString(R.string.num_rooms_preference_key),
                        currentActivity.getApplication());
                ConfigPreferencesModel.updateSelectedPreference(numberOfBathrooms, getString(R.string.num_bathrooms_preference_key),
                        currentActivity.getApplication());
                ConfigPreferencesModel.updateSelectedPreference(orientation, getString(R.string.orientation_preference_key),
                        currentActivity.getApplication());
                ConfigPreferencesModel.updateSelectedPreference(squareMeters, getString(R.string.square_meters_preference_key),
                        currentActivity.getApplication());

            }
        }
    }



}

