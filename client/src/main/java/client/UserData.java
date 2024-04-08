package client;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

public final class UserData{
    // All values in here are default values and will be overwritten at startup if a
    // config file is found. The file regularly gets persisted.

    //INCLUDED IN JSON
    private String token;
    private ArrayDeque<Pair<UUID, String>> recentUUIDs = new ArrayDeque<>();
    private String serverURL = "http://localhost:8080/";
    private String languageCode = "EN";

    private String preferredCurrency = "EUR";

    //NOT INCLUDED IN JSON
    private final static String configFileName = "config.json";
    private final static ObjectMapper objectMapper = new ObjectMapper().enable(INDENT_OUTPUT);
    private final static UserData INSTANCE = new UserData().load();

    private UserData() {}

    private UserData load() {
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
        this.recentUUIDs = userData.recentUUIDs;
        this.serverURL = userData.serverURL;
        this.languageCode = userData.languageCode;
        this.preferredCurrency = userData.preferredCurrency;
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

    public ArrayDeque<Pair<UUID, String>> getRecentUUIDs() {
        return recentUUIDs;
    }

    public void setRecentUUIDs(
        ArrayDeque<Pair<UUID, String>> recentUUIDs) {
        this.recentUUIDs = recentUUIDs;
    }

    @JsonIgnore
    public UUID getCurrentUUID() {
        return getRecentUUIDs().peekFirst().getKey();
    }

    public void setCurrentUUID(Pair<UUID, String> pair) {
        recentUUIDs.removeIf(p -> p.getKey().equals(pair.getKey())); //remove if present
        recentUUIDs.addFirst(pair); //(re-)insert at front
        save(); //save to filesystem
    }

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getCurrencyCode() {
        return preferredCurrency;
    }

    public void setCurrencyCode(String currencyCode) {
        this.preferredCurrency = currencyCode;
    }

    /**
     * custom pair class because the javafx.util.pair class is NOT deserializable
     * @param <K> key
     * @param <V> value
     */
    public static class Pair<K,V> {
        private K key;
        private V value;
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Pair(@JsonProperty("key") K key, @JsonProperty("value") V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}