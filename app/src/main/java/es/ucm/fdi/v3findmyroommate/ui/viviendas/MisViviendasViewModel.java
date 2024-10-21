package es.ucm.fdi.v3findmyroommate.ui.viviendas;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MisViviendasViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MisViviendasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is mis viviendas fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}