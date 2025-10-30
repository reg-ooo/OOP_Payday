package data.model;

public class User {
    private int userID;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String pin;
    private String birthDate;
    private String username;

    public User() {}

    public User(int userID, String fullName, String phoneNumber, String email,
                String pin, String birthDate, String username) {
        this.userID = userID;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.pin = pin;
        this.birthDate = birthDate;
        this.username = username;
    }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
