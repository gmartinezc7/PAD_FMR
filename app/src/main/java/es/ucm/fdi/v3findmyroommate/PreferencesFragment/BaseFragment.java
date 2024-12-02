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

/**
 * BaseFragment es una clase abstracta base para gestionar fragmentos en la aplicación.
 * Contiene métodos reutilizables para la navegación entre fragmentos,
 * la gestión de ChipGroups y validaciones comunes.
 */

public abstract class BaseFragment extends Fragment {
    /**
     * Carga el siguiente fragmento reemplazando el actual.
     *
     * @param nextFragment El fragmento a cargar.
     */
    protected void loadNextFragment(Fragment nextFragment) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, nextFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Actualiza los colores de los Chips en un ChipGroup para reflejar el estado seleccionado.
     *
     * @param group El ChipGroup.
     * @param checkedId El ID del Chip seleccionado.
     */
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
     * Configura un ChipGroup con un listener genérico para manejar selecciones.
     *
     * @param view El View padre que contiene el ChipGroup.
     * @param chipGroupId El ID del ChipGroup.
     * @param onChipSelected Callback para manejar el texto del Chip seleccionado.
     */
    @SuppressWarnings("deprecation")
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

    /**
     * Valida que todos los ChipGroups tengan una selección y que todos los TextInputs requeridos estén llenos.
     *
     * @param requiredChipGroups Lista de ChipGroups requeridos.
     * @param requiredTextInputs Lista de EditTexts requeridos.
     * @return true si todos los campos están llenos; false de lo contrario.
     */
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



    /**
     * Método abstracto para obtener el siguiente fragmento. Cada fragmento hijo debe implementarlo.
     *
     * @return El siguiente fragmento a cargar.
     */
    protected abstract Fragment getNextFragment();


    /**
     * Interfaz funcional para manejar selecciones de Chips.
     */
    protected void updateUserInfoInDatabase() {}
}
