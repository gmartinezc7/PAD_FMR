package es.ucm.fdi.v3findmyroommate.PreferencesFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.Lobby;
import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.SharedViewModel;
import es.ucm.fdi.v3findmyroommate.User;
import es.ucm.fdi.v3findmyroommate.ui.config.ConfigPreferencesModel;

public class RoomPreferencesFragment extends BaseFragment {
    private Button continueButton;
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.room_preferences_fragment, container, false);

        // Initialize the shared ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Setup ChipGroups with a unified listener
        setupChipGroup(view, R.id.maxRoommatesChipGroup, selectedText -> sharedViewModel.setMaxRoommates(selectedText));
        setupChipGroup(view, R.id.roommateGenderChipGroup, selectedText -> sharedViewModel.setRoommateGender(selectedText));
        setupChipGroup(view, R.id.roomTypeChipGroup, selectedText -> sharedViewModel.setOrientation(selectedText));
        setupChipGroup(view, R.id.bathroomChipGroup, selectedText -> sharedViewModel.setBathroomType(selectedText));

        // Setup Continue button
        continueButton = view.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(v -> {
            // Recopilar ChipGroups a validar
            List<ChipGroup> requiredChipGroups = Arrays.asList(
                    view.findViewById(R.id.maxRoommatesChipGroup),
                    view.findViewById(R.id.roommateGenderChipGroup),
                    view.findViewById(R.id.roomTypeChipGroup),
                    view.findViewById(R.id.bathroomChipGroup)
            );

            // Validar antes de continuar
            if (validateSelections(requiredChipGroups, null)) {


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
        return null;
    }


    // Method that updates the user's personal info and its preferences in the database.
    protected void updateUserInfoInDatabase() {
        User userObject = sharedViewModel.getUser().getValue(); // Get user object to obtain its info.

        if (userObject != null) {
            // Get the values for each of the user object's data fields.
            String maxNumberOfRoommates = sharedViewModel.getUser().getValue().getMaxRoommates();
            String roommateGender = sharedViewModel.getUser().getValue().getRoommateGender();
            String orientation = sharedViewModel.getUser().getValue().getOrientation();
            String bathroomType = sharedViewModel.getUser().getValue().getBathroomType();

            Activity currentActivity = getActivity();
            if (currentActivity != null) {
                // Uses ConfigPreferencesModel so that it updates both the shared preferences and the database values.
                ConfigPreferencesModel.updateSelectedPreference(maxNumberOfRoommates, getString(R.string.max_num_roommates_preference_key),
                        currentActivity.getApplication());
                ConfigPreferencesModel.updateSelectedPreference(roommateGender, getString(R.string.roommate_gender_preference_key),
                        currentActivity.getApplication());
                ConfigPreferencesModel.updateSelectedPreference(orientation, getString(R.string.orientation_preference_key),
                        currentActivity.getApplication());
                ConfigPreferencesModel.updateSelectedPreference(bathroomType, getString(R.string.bathroom_type_preference_key),
                        currentActivity.getApplication());
            }
        }
    }


}

