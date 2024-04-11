package client;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

@JsonPropertyOrder({"languageCode", "preferredCurrency", "selectedURL", "urlDataList"})
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE,
                setterVisibility = JsonAutoDetect.Visibility.NONE)
public final class UserData{
    // All values in here are default values and will be overwritten at startup if a
    // config file is found. The file regularly gets persisted.

    //INCLUDED IN JSON
    @JsonProperty
    private String languageCode = "EN";
    @JsonProperty
    private String preferredCurrency = "EUR";
    @JsonProperty
    private String selectedURL = "http://localhost:8080/";
    @JsonProperty
    private ArrayList<UrlData> urlDataList = new ArrayList<>(List.of(new UrlData(selectedURL)));

    //NOT INCLUDED IN JSON
    private final static String configFileName = "config.json";
    private final static ObjectMapper objectMapper = new ObjectMapper().enable(INDENT_OUTPUT);
    private final static UserData INSTANCE = load();

    private UserData() {};

    @JsonCreator
    private UserData(@JsonProperty("languageCode") String languageCode,
                     @JsonProperty("preferredCurrency") String preferredCurrency,
                     @JsonProperty("selectedURL") String selectedURL,
                     @JsonProperty("urlDataList") ArrayList<UrlData> urlDataList) {
        this.languageCode = languageCode;
        this.preferredCurrency = preferredCurrency;
        this.selectedURL = selectedURL;
        this.urlDataList = urlDataList;
    }

    private static UserData load() {
        try {
            return objectMapper.readValue(new File(configFileName), UserData.class);
        } catch (JsonProcessingException e) {
            System.err.println("LOADING ERROR: creating JSON: " + e);
        } catch (IOException e) {
            System.err.println("LOADING ERROR: opening file: " + e);
        }
        return new UserData(); //default values
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

    //getters
    public static UserData getInstance() {
        return INSTANCE;
    }

    public UUID getCurrentUUID() {
        return getRecentUUIDs().peekFirst().getKey();
    }

    private UrlData getUrlData() {
        return urlDataList.stream().filter(urlData -> urlData.url.equals(selectedURL))
                .findAny().get();
    }

    public String getToken() {
        return getUrlData().token;
    }

    public ArrayDeque<Pair<UUID, String>> getRecentUUIDs() {
        return getUrlData().recentUUIDs;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getCurrencyCode() {
        return preferredCurrency;
    }

    public String getServerURL() {
        return selectedURL;
    }

    public List<String> getUrlList() {
        return this.urlDataList.stream().map(urlData -> urlData.url).toList();
    }

    //setters (JSON)
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public void setToken(String u) {
        getUrlData().token = u;
        save();
    }

    public void setCurrentUUID(Pair<UUID, String> pair) {
        getRecentUUIDs().removeIf(p -> p.getKey().equals(pair.getKey())); //remove if present
        getRecentUUIDs().addFirst(pair); //(re-)insert at front
        save(); //save to filesystem
    }

    public void setCurrencyCode(String currencyCode) {
        this.preferredCurrency = currencyCode;
    }

    public void setSelectedURL(String selectedURL) {
        this.selectedURL = selectedURL;
        try {
            getUrlData();
        } catch (NoSuchElementException e) {
            urlDataList.add(new UrlData(selectedURL));
        }
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

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static final class UrlData {
        private final String url;
        private final ArrayDeque<Pair<UUID, String>> recentUUIDs;
        private String token;

        @JsonCreator
        private UrlData(@JsonProperty("url") String url,
                        @JsonProperty("recentUUIDs") ArrayDeque<Pair<UUID, String>> recentUUIDs,
                        @JsonProperty("token") String token) {
            this.url = url;
            this.recentUUIDs = recentUUIDs;
            this.token = token;
        }

        private UrlData(String url) {
            this.url = url;
            this.recentUUIDs = new ArrayDeque<>();
        }
    }
}