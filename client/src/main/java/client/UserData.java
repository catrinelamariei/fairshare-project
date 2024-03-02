package client;

public final class UserData {

    private String token;
    private final static UserData INSTANCE = new UserData();

    private UserData() {}

    public static UserData getInstance() {
        return INSTANCE;
    }

    public void setToken(String u) {
        this.token = u;
    }

    public String getToken() {
        return this.token;
    }
}