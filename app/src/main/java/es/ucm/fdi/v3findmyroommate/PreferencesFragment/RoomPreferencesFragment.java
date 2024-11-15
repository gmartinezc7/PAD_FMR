package es.ucm.fdi.v3findmyroommate.PreferencesFragment;

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

import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.SharedViewModel;

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
                loadNextFragment(getNextFragment()); // Continuar si la validación es exitosa
            }
        });

        return view;
    }


    @Override
    protected Fragment getNextFragment() {
        return  new PersonalInformationFragment(); // Change this to the actual next fragment
    }


}

