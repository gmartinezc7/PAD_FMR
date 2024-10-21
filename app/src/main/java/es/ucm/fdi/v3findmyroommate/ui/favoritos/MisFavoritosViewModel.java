package es.ucm.fdi.v3findmyroommate.ui.favoritos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MisFavoritosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MisFavoritosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is mis favoritos fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}