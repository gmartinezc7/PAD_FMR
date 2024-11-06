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

import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.SharedViewModel;

public class PersonalInformationFragment extends BaseFragment {
    private Button continueButton;
    private ChipGroup genderChipGroup;
    private ChipGroup ageChipGroup;
    private SharedViewModel sharedViewModel;
    private ChipGroup maritalStatusChipGroup;
    private ChipGroup occupationChipGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout del fragmento
        View view = inflater.inflate(R.layout.personal_information_fragment, container, false);

        // Inicializa el ViewModel compartido
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Configuración de ChipGroup para selección de género
        genderChipGroup = view.findViewById(R.id.genderChipGroup);
        genderChipGroup.setOnCheckedChangeListener(this::handleGenderSelection);

        // Configuración de ChipGroup para selección de edad
        ageChipGroup = view.findViewById(R.id.ageChipGroup);
        ageChipGroup.setOnCheckedChangeListener(this::handleAgeSelection);

        // Configuración de ChipGroup para selección de estado civil
        maritalStatusChipGroup = view.findViewById(R.id.maritalStatusChipGroup);
        maritalStatusChipGroup.setOnCheckedChangeListener(this::handleMaritalStatusSelection);

        // Configuración de ChipGroup para selección de ocupación
        occupationChipGroup = view.findViewById(R.id.occupationChipGroup);
        occupationChipGroup.setOnCheckedChangeListener(this::handleOccupationSelection);

        // Configura el botón Continuar
        continueButton = view.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(v -> loadNextFragment(getNextFragment()));

        return view;
    }

    private void handleGenderSelection(ChipGroup group, int checkedId) {
        // Si no hay selección, se hace return
        if (checkedId == -1) return;

        String gender = null;
        updateChipColors(group, checkedId);
        if (checkedId == R.id.chipMale) {
            gender = "Male";
        } else if (checkedId == R.id.chipFemale) {
            gender = "Female";
        }
        if (gender != null) {
            sharedViewModel.setGender(gender); // Guarda en el ViewModel
            Log.d("GenderSelection", gender + " selected");
        }
    }

    private void handleAgeSelection(ChipGroup group, int checkedId) {
        // Si no hay selección, se hace return
        if (checkedId == -1) return;

        String ageRange = null;
        updateChipColors(group, checkedId);
        if (checkedId == R.id.chip18To25) {
            ageRange = "18-25";
        } else if (checkedId == R.id.chip25To35) {
            ageRange = "25-35";
        } else if (checkedId == R.id.chip35To45) {
            ageRange = "35-45";
        } else if (checkedId == R.id.chipOver45) {
            ageRange = ">45";
        }
        if (ageRange != null) {
            sharedViewModel.setAgeRange(ageRange); // Guarda en el ViewModel
            Log.d("AgeSelection", ageRange + " selected");
        }
    }

    private void handleMaritalStatusSelection(ChipGroup group, int checkedId) {
        if (checkedId == -1) return;

        String maritalStatus = null;
        updateChipColors(group, checkedId);
        if (checkedId == R.id.chipSingle) {
            maritalStatus = "Single";
        } else if (checkedId == R.id.chipInARelationship) {
            maritalStatus = "In a Relationship";
        } else if (checkedId == R.id.chipMarried) {
            maritalStatus = "Married";
        } else if (checkedId == R.id.chipPreferNotToSay) {
            maritalStatus = "Prefer Not to Say";
        }
        if (maritalStatus != null) {
            sharedViewModel.setMaritalStatus(maritalStatus);
            Log.d("MaritalStatusSelection", maritalStatus + " selected");
        }
    }

    private void handleOccupationSelection(ChipGroup group, int checkedId) {
        if (checkedId == -1) return;

        String occupation = null;
        updateChipColors(group, checkedId);
        if (checkedId == R.id.chipEmployed) {
            occupation = "Employed";
        } else if (checkedId == R.id.chipUnemployed) {
            occupation = "Unemployed";
        } else if (checkedId == R.id.chipStudent) {
            occupation = "Student";
        } else if (checkedId == R.id.chipRetired) {
            occupation = "Retired";
        }
        if (occupation != null) {
            sharedViewModel.setOccupation(occupation);
            Log.d("OccupationSelection", occupation + " selected");
        }
    }


    private void updateChipColors(ChipGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View chip = group.getChildAt(i);
            if (chip instanceof Chip) {
                if (chip.getId() == checkedId) {
                    // Chip seleccionado
                    chip.setBackgroundColor(getResources().getColor(R.color.chipSelectedColor));
                } else {
                    // Chip no seleccionado
                    chip.setBackgroundColor(getResources().getColor(R.color.chipDefaultColor));
                }
            }
        }
    }

    @Override
    protected Fragment getNextFragment() {
        // Devuelve el siguiente fragmento que deseas cargar
        return null;
    }
}
