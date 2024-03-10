package client;

import java.util.HashSet;
import java.util.Set;

public final class UserData {

    private String token;
    private String currentUUID;
    private Set<String> recentUUIDs = new HashSet<>();

    private String serverUrl = "http://localhost:8080";
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

    public Set<String> getRecentUUIDs() {
        return recentUUIDs;
    }

    public void setRecentUUIDs(Set<String> recentUUIDs) {
        this.recentUUIDs = recentUUIDs;
    }

    public String getCurrentUUID() {
        return currentUUID;
    }

    public void setCurrentUUID(String currentUUID) {
        recentUUIDs.add(currentUUID);
        this.currentUUID = currentUUID;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}