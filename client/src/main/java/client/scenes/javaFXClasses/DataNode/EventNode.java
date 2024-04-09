package client.scenes.javaFXClasses.DataNode;

import client.MainCtrl;
import client.utils.EventJson.EventJsonUtil;
import javafx.scene.control.TitledPane;

import java.util.UUID;

import static client.UserData.Pair;

public class EventNode extends TitledPane {
    protected final MainCtrl mainCtrl;
    protected final EventJsonUtil jsonUtil;
    protected final Pair<UUID, String> idNamePair;

    protected EventNode(MainCtrl mainCtrl, EventJsonUtil jsonUtil, Pair<UUID, String> idNamePair) {
        super();
        this.mainCtrl = mainCtrl;
        this.jsonUtil = jsonUtil;
        this.idNamePair = idNamePair;
    }

    public Pair<UUID, String> getPair() {
        return idNamePair;
    }
}
