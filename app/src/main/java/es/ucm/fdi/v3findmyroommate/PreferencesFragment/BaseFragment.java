package es.ucm.fdi.v3findmyroommate.PreferencesFragment;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import es.ucm.fdi.v3findmyroommate.R;

public abstract class BaseFragment extends Fragment {

    protected void loadNextFragment(Fragment nextFragment) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, nextFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


    protected void updateChipColors(ChipGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View chip = group.getChildAt(i);
            if (chip instanceof Chip) {
                Chip materialChip = (Chip) chip;
                if (chip.getId() == checkedId) {
                    // Chip seleccionado
                    materialChip.setChipBackgroundColorResource(R.color.chip_selected);
                } else {
                    // Chip no seleccionado
                    materialChip.setChipBackgroundColorResource(R.color.chip_unselected);
                }
            }
        }
    }



    /**
     * Sets up a ChipGroup with a generic listener to reduce redundancy.
     * @param view The parent view.
     * @param chipGroupId The ID of the ChipGroup.
     * @param onChipSelected A callback to handle the selected chip text.
     */
    protected void setupChipGroup(View view, int chipGroupId, OnChipSelectedCallback onChipSelected) {
        ChipGroup chipGroup = view.findViewById(chipGroupId);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                Chip selectedChip = group.findViewById(checkedId);
                if (selectedChip != null) {
                    String selectedText = selectedChip.getText().toString();
                    onChipSelected.onChipSelected(selectedText);
                    Log.d("ChipSelection", "Selected: " + selectedText);
                    updateChipColors(group, checkedId);
                }
            }
        });
    }
    /**
     * Functional interface for handling chip selection.
     */
    protected interface OnChipSelectedCallback {
        void onChipSelected(String selectedText);
    }

    protected boolean validateSelections(List<ChipGroup> requiredChipGroups, List<EditText> requiredTextInputs) {
        // Verifica si hay algún ChipGroup sin seleccionar
        for (ChipGroup chipGroup : requiredChipGroups) {
            if (chipGroup.getCheckedChipId() == -1) {
                Toast.makeText(getContext(), "Por favor, selecciona todas las opciones requeridas", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        // Verifica si hay algún TextInputEditText vacío, solo si se proporciona la lista
        if (requiredTextInputs != null) {
            for (EditText textInput : requiredTextInputs) {
                if (textInput.getText() == null || textInput.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), "Por favor, completa todos los campos de texto requeridos", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        return true; // Todos los campos están llenos
    }



    // Método abstracto que cada fragmento hijo deberá implementar para obtener el siguiente fragmento
    protected abstract Fragment getNextFragment();


    // Method that's used in some of the fragments, to update the user's information to the database.
    protected void updateUserInfoInDatabase() {}
}
