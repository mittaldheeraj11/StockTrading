package entity;

public class User {
    private String  userId;

    private String userName;

    private String phoneNumber;

    private String  emailId;

    public User(String userId, String userName, String phoneNumber, String emailId) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }
}
