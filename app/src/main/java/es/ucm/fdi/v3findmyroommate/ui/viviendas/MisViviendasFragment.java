package es.ucm.fdi.v3findmyroommate.ui.viviendas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.v3findmyroommate.databinding.FragmentMisViviendasBinding;

public class MisViviendasFragment extends Fragment {

    private FragmentMisViviendasBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MisViviendasViewModel misViviendasViewModel =
                new ViewModelProvider(this).get(MisViviendasViewModel.class);

        binding = FragmentMisViviendasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMisViviendas;
        misViviendasViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}