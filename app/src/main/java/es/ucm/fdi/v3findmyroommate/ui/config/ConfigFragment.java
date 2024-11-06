package es.ucm.fdi.v3findmyroommate.ui.config;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.v3findmyroommate.R;
import es.ucm.fdi.v3findmyroommate.databinding.FragmentConfigBinding;

public class ConfigFragment extends Fragment {

    private FragmentConfigBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ConfigViewModel configViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()))
                .get(ConfigViewModel.class);

        binding = FragmentConfigBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Add preferences fragment here instead of on item click
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.preferences_frame, new ConfigEditTextPreferencesFragment(configViewModel))
                .commit();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}