package client;

import java.util.ArrayDeque;
import java.util.UUID;


public interface UserDataInterface {

    void setToken(String u);
    String getToken();
    ArrayDeque<UserData.Pair<UUID, String>> getRecentUUIDs();
    void setRecentUUIDs(ArrayDeque<UserData.Pair<UUID, String>> recentUUIDs);
    UUID getCurrentUUID();
    void setCurrentUUID(UserData.Pair<UUID, String> pair);
    void save();
    String getServerURL();
    void setServerURL(String serverURL);
}
