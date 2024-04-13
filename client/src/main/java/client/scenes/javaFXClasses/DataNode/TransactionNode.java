package client.scenes.javaFXClasses.DataNode;

import client.UserData;
import client.scenes.EventPageCtrl;
import client.utils.ServerUtils;
import javafx.scene.layout.HBox;

import java.util.UUID;

public class TransactionNode extends HBox {
    protected final EventPageCtrl eventPageCtrl;
    protected final ServerUtils server;
    protected final UserData userData;

    public final UUID id;

    protected TransactionNode(EventPageCtrl eventPageCtrl, ServerUtils server, UUID id,
                              UserData userData) {
        this.eventPageCtrl = eventPageCtrl;
        this.server = server;
        this.userData = userData;
        this.id = id;
    }
}
