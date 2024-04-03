package client.scenes.javaFXClasses.DataNode;

import javafx.scene.control.TitledPane;
import client.MainCtrl;

import java.util.UUID;

import static client.UserData.Pair;

public class EventNode extends TitledPane {
    protected final MainCtrl mainCtrl;
    protected final Pair<UUID, String> idNamePair;

    protected EventNode(MainCtrl mainCtrl, Pair<UUID, String> idNamePair) {
        super();
        this.mainCtrl = mainCtrl;
        this.idNamePair = idNamePair;
    }
}
