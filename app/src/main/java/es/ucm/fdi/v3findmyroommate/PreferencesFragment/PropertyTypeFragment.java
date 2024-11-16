package es.ucm.fdi.v3findmyroommate.PreferencesFragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.SharedViewModel;

public class PropertyTypeFragment extends BaseFragment {
    private Button continueButton;
    private SharedViewModel sharedViewModel;
    private ChipGroup propertyTypeChipGroup;
    private TextInputLayout maxBudgetTextInputLayout;
    private TextInputEditText maxBudgetEditText;
    private String propertyType = null;
    private View view = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.property_type_fragment, container, false);
        // Recuperar nuestros datos de user
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        setupChipGroup(view, R.id.propertyTypeChipGroup, selectedText -> sharedViewModel.setPropertyType(propertyType));


        maxBudgetTextInputLayout = view.findViewById(R.id.maxBudgetTextInputLayout);
        maxBudgetEditText = view.findViewById(R.id.maxBudgetInput);

        maxBudgetEditText = view.findViewById(R.id.maxBudgetInput);

        continueButton = view.findViewById(R.id.continueButton);

        continueButton.setOnClickListener(v -> {
            // Recopilar ChipGroups y TextInputEditText a validar
            List<ChipGroup> requiredChipGroups = Arrays.asList(view.findViewById(R.id.propertyTypeChipGroup));
            List<TextInputEditText> requiredTextInputs = Arrays.asList(maxBudgetEditText);
            if (validateSelections(requiredChipGroups, requiredTextInputs)) {
                String maxBudget = maxBudgetEditText.getText().toString();
                sharedViewModel.setMaxBudget(maxBudget);
                loadNextFragment(getNextFragment()); // Continuar si la validación es exitosa
            }
        });


        return view;
    }


    @Override
    protected Fragment getNextFragment() {

        propertyType = getPropertyType();
        return propertyType.equals(getString(R.string.house_property_type_label)) ? new HousePreferencesFragment() : new RoomPreferencesFragment();
    }
    private String getPropertyType(){
        ChipGroup propertyTypeChipGroup = view.findViewById(R.id.propertyTypeChipGroup);
        int selectedChipId = propertyTypeChipGroup.getCheckedChipId();
        Chip selectedChip = view.findViewById(selectedChipId);
        propertyType = selectedChip.getText().toString();

        return propertyType;
    }

}
