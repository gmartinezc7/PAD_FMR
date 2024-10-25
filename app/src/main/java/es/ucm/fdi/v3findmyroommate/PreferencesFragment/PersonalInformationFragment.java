package es.ucm.fdi.v3findmyroommate.PreferencesFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButtonToggleGroup;

import es.ucm.fdi.v3findmyroommate.R;

public class PersonalInformationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout del fragmento
        View view = inflater.inflate(R.layout.personal_information_fragment, container, false);

        // Usa `view.findViewById` para acceder a los componentes del layout
        MaterialButtonToggleGroup segmentedGroup = view.findViewById(R.id.genderButtonToggle);

        segmentedGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    /*switch (checkedId) {
                        case R.id.button1:
                            // Acción para la opción 1
                            break;
                        case R.id.button2:
                            // Acción para la opción 2
                            break;
                    }

                     */
                }
            }
        });

        return view;  // Devuelve la vista inflada
    }
}
