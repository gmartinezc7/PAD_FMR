package es.ucm.fdi.v3findmyroommate;

public class User {
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String rangeAge;
    private String gender;
    private String maritalStatus;
    private String occupation;
    private String propertyType;
    private String maxRoomates;
    private String roomateGender;
    private String bathroomType;
    private String rooms;
    private String bathrooms;
    private String orientation;
    private String squareMeters;
    private String maxBudget;

    // Default constructor
    public User() {}

    // Getters and Setters for each attribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRangeAge() {
        return rangeAge;
    }

    public void setRangeAge(String rangeAge) {
        this.rangeAge = rangeAge;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getMaxRoommates() {
        return maxRoomates;
    }

    public void setMaxRoommates(String maxRoommates) {
        this.maxRoomates = maxRoommates;
    }

    public String getRoommateGender() {
        return roomateGender;
    }

    public void setRoommateGender(String roommateGender) {
        this.roomateGender = roommateGender;
    }

    public String getBathroomType() {
        return bathroomType;
    }

    public void setBathroomType(String bathroomType) {
        this.bathroomType = bathroomType;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(String bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(String squareMeters) {
        this.squareMeters = squareMeters;
    }

    public void setMaxBudget(String maxBudget) {this.maxBudget = maxBudget; }

    public String getmaxBudget() {return this.maxBudget ;  }
}
