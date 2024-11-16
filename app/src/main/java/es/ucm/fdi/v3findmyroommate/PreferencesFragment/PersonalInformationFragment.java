package es.ucm.fdi.v3findmyroommate.PreferencesFragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.SharedViewModel;
import es.ucm.fdi.v3findmyroommate.User;

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

        // Configura cada ChipGroup con su respectiva lógica
        setupChipGroup(view, R.id.genderChipGroup, (selectedValue) -> sharedViewModel.setGender(selectedValue));
        setupChipGroup(view, R.id.ageChipGroup, (selectedValue) -> sharedViewModel.setAgeRange(selectedValue));
        setupChipGroup(view, R.id.maritalStatusChipGroup, (selectedValue) -> sharedViewModel.setMaritalStatus(selectedValue));
        setupChipGroup(view, R.id.occupationChipGroup, (selectedValue) -> sharedViewModel.setOccupation(selectedValue));

        // Configura el botón Continuar
        continueButton = view.findViewById(R.id.continueButton);

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




}
