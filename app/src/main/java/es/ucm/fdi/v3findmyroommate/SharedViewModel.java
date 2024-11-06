package es.ucm.fdi.v3findmyroommate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<User> user = new MutableLiveData<>(new User()); // Aseguramos una instancia inicial de User

    public void setUser(User user) {
        this.user.setValue(user);
    }

    public LiveData<User> getUser() {
        return user;
    }

    // Método para actualizar solo el género del usuario
    public void setGender(String gender) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setGender(gender);
            user.setValue(currentUser); // Actualizamos el MutableLiveData con el nuevo valor
        }
    }

    // Método para actualizar solo el rango de edad del usuario
    public void setAgeRange(String ageRange) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setRangeAge(ageRange);
            user.setValue(currentUser); // Actualizamos el MutableLiveData con el nuevo valor
        }
    }
    // Método para actualizar solo el estado civil del usuario
    public void setMaritalStatus(String maritalStatus) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setMaritalStatus(maritalStatus);
            user.setValue(currentUser);
        }
    }

    // Método para actualizar solo la ocupación del usuario
    public void setOccupation(String occupation) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setOccupation(occupation);
            user.setValue(currentUser);
        }
    }
}

