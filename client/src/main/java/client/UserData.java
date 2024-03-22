package client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

public final class UserData {
    private final static String configFileName = "config.json";

    // All values in here are default values and will be overwritten at startup if a
    // config file is found when the program terminates, all values are stored in the config file
    private String token;
    private UUID currentUUID;

    private Set<UUID> recentUUIDs = new HashSet<>();

    private String serverURL = "http://localhost:8080/";
    private final static ObjectMapper objectMapper = new ObjectMapper().enable(INDENT_OUTPUT);
    private final static UserData INSTANCE = new UserData().load();

    private UserData() {}

    public UserData load() {
        try {
            this.update(objectMapper.readValue(new File(configFileName), UserData.class));
        } catch (JsonProcessingException e) {
            System.err.println("LOADING ERROR: creating JSON: " + e);
        } catch (IOException e) {
            System.err.println("LOADING ERROR: opening file: " + e);
        }
        return this;
    }

    public void save() {
        try {
            objectMapper.writeValue(new File(configFileName), this);
        } catch (JsonProcessingException e) {
            System.err.println("SAVING ERROR: creating json: " + e);
        } catch (IOException e) {
            System.err.println("SAVING ERROR: opening file: " + e);
        }
    }

    /**
     * overwrites all fields because instance might be saved in which
     * case overwriting instance wouldn't suffice
     * @param userData data source
     */
    private void update(UserData userData) {
        this.token = userData.token;
        this.currentUUID = userData.currentUUID;
        this.recentUUIDs = userData.recentUUIDs;
        this.serverURL = userData.serverURL;
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