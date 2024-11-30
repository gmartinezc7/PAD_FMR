package es.ucm.fdi.v3findmyroommate.PreferencesFragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.SharedViewModel;
import es.ucm.fdi.v3findmyroommate.User;
import es.ucm.fdi.v3findmyroommate.ui.config.ConfigPreferencesModel;

public class PropertyTypeFragment extends BaseFragment {
    private Button continueButton;
    private SharedViewModel sharedViewModel;
    private ChipGroup propertyTypeChipGroup;
    private EditText maxBudgetEditText;
    private String propertyType;
    private View view = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.property_type_fragment, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        // Recuperar nuestros datos de user
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        setupChipGroup(view, R.id.propertyTypeChipGroup, selectedText -> sharedViewModel.setPropertyType(propertyType));



        maxBudgetEditText = view.findViewById(R.id.maxBudgetInput);

        continueButton = view.findViewById(R.id.continueButton);

        continueButton.setOnClickListener(v -> {
            // Recopilar ChipGroups y TextInputEditText a validar
            List<ChipGroup> requiredChipGroups = Arrays.asList(view.findViewById(R.id.propertyTypeChipGroup));
            List<EditText> requiredTextInputs = Arrays.asList(maxBudgetEditText);
            if (validateSelections(requiredChipGroups, requiredTextInputs)) {
                this.propertyType = getPropertyType();
                String maxBudget = maxBudgetEditText.getText().toString();
                sharedViewModel.setPropertyType(this.propertyType);
                sharedViewModel.setMaxBudget(maxBudget);
                updateUserInfoInDatabase();
                loadNextFragment(getNextFragment()); // Continuar si la validación es exitosa
            }
        });


        return view;
    }


    @Override
    protected Fragment getNextFragment() {
        return this.propertyType.equals(getString(R.string.house_property_type_label)) ? new HousePreferencesFragment() : new RoomPreferencesFragment();
    }


    private String getPropertyType(){
        ChipGroup propertyTypeChipGroup = view.findViewById(R.id.propertyTypeChipGroup);
        int selectedChipId = propertyTypeChipGroup.getCheckedChipId();
        Chip selectedChip = view.findViewById(selectedChipId);
        this.propertyType = selectedChip.getText().toString();
        return propertyType;
    }


    // Method that updates the user's personal info and its preferences in the database.
    protected void updateUserInfoInDatabase() {
        User userObject = sharedViewModel.getUser().getValue(); // Get user object to obtain its info.

        if (userObject != null) {
            // Get the values for each of the user object's data fields.
            String propertyType = sharedViewModel.getUser().getValue().getPropertyType();
            String maxBudget = sharedViewModel.getUser().getValue().getmaxBudget();

            Activity currentActivity = getActivity();
            if (currentActivity != null) {

                // Uses ConfigPreferencesModel so that it updates both the shared preferences and the database values.
                ConfigPreferencesModel.updateSelectedPreference(propertyType, getString(R.string.property_type_preference_key),
                        currentActivity.getApplication());
                ConfigPreferencesModel.updateSelectedPreference(maxBudget, getString(R.string.max_budget_preference_key),
                        currentActivity.getApplication());
            }


        }
    }

}
