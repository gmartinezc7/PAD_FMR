package es.ucm.fdi.v3findmyroommate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<User> user = new MutableLiveData<>(new User()); // Ensure an initial instance of User

    public void setUser(User user) {
        this.user.setValue(user);
    }

    public LiveData<User> getUser() {
        return user;
    }

    // Existing setters for specific fields in User
    public void setGender(String gender) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setGender(gender);
            user.setValue(currentUser);
        }
    }

    public void setAgeRange(String ageRange) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setRangeAge(ageRange);
            user.setValue(currentUser);
        }
    }

    public void setMaritalStatus(String maritalStatus) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setMaritalStatus(maritalStatus);
            user.setValue(currentUser);
        }
    }

    public void setOccupation(String occupation) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setOccupation(occupation);
            user.setValue(currentUser);
        }
    }

    public void setPropertyType(String propertyType) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setPropertyType(propertyType);
            user.setValue(currentUser);
        }
    }

    public void setMaxRoommates(String maxRoommates) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setMaxRoommates(maxRoommates);
            user.setValue(currentUser);
        }
    }

    public void setRoommateGender(String roommateGender) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setRoommateGender(roommateGender);
            user.setValue(currentUser);
        }
    }



    public void setBathroomType(String bathroomType) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setBathroomType(bathroomType);
            user.setValue(currentUser);
        }
    }

    // New setters for rooms, bathrooms, orientation, and square meters
    public void setNumberOfRooms(String rooms) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setRooms(rooms);
            user.setValue(currentUser);
        }
    }

    public void setNumberOfBathrooms(String bathrooms) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setBathrooms(bathrooms);
            user.setValue(currentUser);
        }
    }

    public void setOrientation(String orientation) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setOrientation(orientation);
            user.setValue(currentUser);
        }
    }

    public void setSquareMeters(String squareMeters) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setSquareMeters(squareMeters);
            user.setValue(currentUser);
        }
    }

    public void setMaxBudget(String maxBudget) {
        User currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setMaxBudget(maxBudget);
            user.setValue(currentUser);
        }
    }
}
