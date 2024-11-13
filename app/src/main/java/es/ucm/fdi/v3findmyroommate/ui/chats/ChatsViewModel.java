package es.ucm.fdi.v3findmyroommate.ui.chats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChatsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ChatsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Chats disponibles");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
