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

    // Constructor por defecto
    public User() {}

    // Getters y Setters para cada atributo
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
}
