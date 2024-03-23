package client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class UserData {

    private String token;
    private UUID currentUUID;

    private List<UUID> recentUUIDs = new ArrayList<>();

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

    public List<UUID> getRecentUUIDs() {
        return recentUUIDs;
    }


    public void addRecentUUID(UUID uuid) {
        // if it's already a recent one: move it to the front
        if (recentUUIDs.contains(uuid)) {
            recentUUIDs.remove(uuid);
            recentUUIDs.add(0, uuid);
        // append to front it if it's a new one
        } else recentUUIDs.add(0, uuid);
        // store only the 5 most recent events
        if (recentUUIDs.size() > 5) {
            recentUUIDs.removeLast();
        }
    }

    public UUID getCurrentUUID() {
        return currentUUID;
    }

    public void setCurrentUUID(UUID currentUUID) {
        addRecentUUID(currentUUID);
        this.currentUUID = currentUUID;
    }

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }
}