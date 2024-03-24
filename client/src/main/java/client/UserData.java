package client;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class UserData {

    private String token;
    private UUID currentUUID=UUID.randomUUID();

    private Set<UUID> recentUUIDs = new HashSet<>();

    private String serverURL = "http://localhost:8080/";
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

    public Set<UUID> getRecentUUIDs() {
        return recentUUIDs;
    }

    public void setRecentUUIDs(Set<UUID> recentUUIDs) {
        this.recentUUIDs = recentUUIDs;
    }

    public UUID getCurrentUUID() {
        return currentUUID;
    }

    public void setCurrentUUID(UUID currentUUID) {
        recentUUIDs.add(currentUUID);
        this.currentUUID = currentUUID;
    }

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }
}