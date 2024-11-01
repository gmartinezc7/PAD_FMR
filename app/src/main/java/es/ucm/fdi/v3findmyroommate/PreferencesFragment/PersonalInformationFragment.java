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

import com.google.android.material.chip.ChipGroup;

import es.ucm.fdi.v3findmyroommate.R;

public class PersonalInformationFragment extends BaseFragment {
    private Button continueButton;
    private ChipGroup genderChipGroup;
    private ChipGroup ageChipGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout del fragmento
        View view = inflater.inflate(R.layout.personal_information_fragment, container, false);

        // Configuración de ChipGroup para selección de género
        genderChipGroup = view.findViewById(R.id.genderChipGroup);
        genderChipGroup.setOnCheckedChangeListener(this::handleGenderSelection);

        // Configuración de ChipGroup para selección de edad
        ageChipGroup = view.findViewById(R.id.ageChipGroup);
        ageChipGroup.setOnCheckedChangeListener(this::handleAgeSelection);

        // Configura el botón Continuar
        continueButton = view.findViewById(R.id.continueButton); // Asegúrate de que este ID coincide con tu layout
        continueButton.setOnClickListener(v -> loadNextFragment(getNextFragment()));

        return view;
    }

    private void handleGenderSelection(ChipGroup group, int checkedId) {
        if (checkedId != -1) { // -1 indica que no hay chip seleccionado
            if (checkedId == R.id.chipMale) {
                Log.d("GenderSelection", "Male selected");
            } else if (checkedId == R.id.chipFemale) {
                Log.d("GenderSelection", "Female selected");
            }
        }
    }

    private void handleAgeSelection(ChipGroup group, int checkedId) {
        if (checkedId != -1) { // -1 indica que no hay chip seleccionado
             if (checkedId == R.id.chip18To25) {
                Log.d("AgeSelection", "18-25 selected");
            } else if (checkedId == R.id.chip25To35) {
                Log.d("AgeSelection", "25-35 selected");
            } else if (checkedId == R.id.chip35To45) {
                Log.d("AgeSelection", "35-45 selected");
            } else if (checkedId == R.id.chipOver45) {
                Log.d("AgeSelection", ">45 selected");
            }
        }
    }

    @Override
    protected Fragment getNextFragment() {
        // Devuelve el siguiente fragmento que deseas cargar
        return null;
    }
}
