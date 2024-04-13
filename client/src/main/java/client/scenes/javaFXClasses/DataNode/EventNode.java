package client.scenes.javaFXClasses.DataNode;

import client.MainCtrl;
import client.UserData;
import client.utils.EventJsonUtil;
import javafx.scene.control.TitledPane;

import java.util.UUID;

import static client.UserData.Pair;

public class EventNode extends TitledPane {
    protected final MainCtrl mainCtrl;
    protected final EventJsonUtil jsonUtil;
    protected final UserData userData;
    protected final Pair<UUID, String> idNamePair;

    protected EventNode(MainCtrl mainCtrl, EventJsonUtil jsonUtil, UserData userData, Pair<UUID,
            String> idNamePair) {
        super();
        this.mainCtrl = mainCtrl;
        this.jsonUtil = jsonUtil;
        this.idNamePair = idNamePair;
        this.userData = userData;
    }

    public Pair<UUID, String> getPair() {
        return idNamePair;
    }
}
