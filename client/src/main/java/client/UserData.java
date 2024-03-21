package client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class UserData {
    // All values in here are default values and will be overwritten at startup if a config file is found
    // when the program terminates, all values are stored in the config file
    private String token;
    private UUID currentUUID;

    private Set<UUID> recentUUIDs = new HashSet<>();

    private String serverURL = "http://localhost:8080/";
    private final static UserData INSTANCE = new UserData();
    private final static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * tries to instantiate all data using config file from filesystem
     */
    private UserData() {
        // TODO: load fields from JSON
    }

    public void save() {
        // TODO: write fields to JSON

    }

    @JsonIgnore
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