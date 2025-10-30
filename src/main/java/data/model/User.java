package data.model;

public class User {
    private static int userID;
    private static String fullName;
    private static String phoneNumber;
    private static String email;
    private static String pin;
    private static String birthDate;
    private static String username;

    public User() {}

    public static void setUser(int userID, String fullName, String phoneNumber,
                               String email, String pin, String birthDate, String username) {
        User.userID = userID;
        User.fullName = fullName;
        User.phoneNumber = phoneNumber;
        User.email = email;
        User.pin = pin;
        User.birthDate = birthDate;
        User.username = username;
    }

    public static int getUserID() { return userID; }
    public static void setUserID(int userID) { User.userID = userID; }

    public static String getFullName() { return fullName; }
    public static void setFullName(String fullName) { User.fullName = fullName; }

    public static String getPhoneNumber() { return phoneNumber; }
    public static void setPhoneNumber(String phoneNumber) { User.phoneNumber = phoneNumber; }

    public static String getEmail() { return email; }
    public static void setEmail(String email) { User.email = email; }

    public static String getPin() { return pin; }
    public static void setPin(String pin) { User.pin = pin; }

    public static String getBirthDate() { return birthDate; }
    public static void setBirthDate(String birthDate) { User.birthDate = birthDate; }

    public static String getUsername() { return username; }
    public static void setUsername(String username) { User.username = username; }

    public static void logout() {
        User.userID = -2;  // Set to 0 or -1 to indicate no user
        User.fullName = null;
        User.phoneNumber = null;
        User.email = null;
        User.pin = null;
        User.birthDate = null;
        User.username = null;
    }

    public static boolean isLoggedIn() {
        return userID != -2;  // or check if username != null
    }
}