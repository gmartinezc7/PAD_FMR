package es.ucm.fdi.v3findmyroommate.ui.favoritos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.v3findmyroommate.databinding.FragmentMisFavoritosBinding;

public class MisFavoritosFragment extends Fragment {

    private FragmentMisFavoritosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MisFavoritosViewModel misFavoritosViewModel =
                new ViewModelProvider(this).get(MisFavoritosViewModel.class);

        binding = FragmentMisFavoritosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMisFavoritos;
        misFavoritosViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}