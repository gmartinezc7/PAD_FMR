package es.ucm.fdi.v3findmyroommate.PreferencesFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.SharedViewModel;

public class HousePreferencesFragment extends BaseFragment {

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
                loadNextFragment(getNextFragment()); // Si la validación es correcta, continúa
            }
        });





        return view;
    }


    @Override
    protected Fragment getNextFragment() {
        return null; // Replace with the actual next fragment
    }




}

