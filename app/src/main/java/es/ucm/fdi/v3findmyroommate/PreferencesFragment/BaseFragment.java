package es.ucm.fdi.v3findmyroommate.PreferencesFragment;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

    // Método abstracto que cada fragmento hijo deberá implementar para obtener el siguiente fragmento
    protected abstract Fragment getNextFragment();
}
