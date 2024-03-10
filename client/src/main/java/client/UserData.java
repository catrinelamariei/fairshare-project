package client;

import java.util.ArrayList;

public final class UserData {

    private String token;
    private String currentUUID;
    private ArrayList<String> recentUUIDs;
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

    public ArrayList<String> getRecentUUIDs() {
        return recentUUIDs;
    }

    public void setRecentUUIDs(ArrayList<String> recentUUIDs) {
        this.recentUUIDs = recentUUIDs;
    }

    public String getCurrentUUID() {
        return currentUUID;
    }

    public void setCurrentUUID(String currentUUID) {
        this.currentUUID = currentUUID;
    }
}