package client.scenes.javaFXClasses.DataNode;

import client.UserData;
import client.scenes.EventPageCtrl;
import client.utils.ServerUtils;
import javafx.scene.control.TitledPane;

import java.util.UUID;

public class ParticipantNode extends TitledPane {
    //services
    protected final EventPageCtrl eventPageCtrl;
    protected final UserData userData;
    protected final ServerUtils serverUtils;

    public final UUID id; //to address participant contained in this node

    protected ParticipantNode(UUID id, String fullName, EventPageCtrl eventPageCtrl,
                              UserData userData, ServerUtils serverUtils) {
        super(fullName, null);
        this.id = id;
        this.eventPageCtrl = eventPageCtrl;
        this.userData = userData;
        this.serverUtils = serverUtils;
    }
}
