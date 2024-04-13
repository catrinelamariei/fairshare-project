package client.scenes.javaFXClasses.DataNode;

import client.UserData;
import client.scenes.EventPageCtrl;
import javafx.scene.control.TitledPane;

import java.util.UUID;

public class ParticipantNode extends TitledPane {
    //services
    protected final EventPageCtrl eventPageCtrl;
    protected final UserData userData;

    public final UUID id; //to address participant contained in this node

    protected ParticipantNode(UUID id, String fullName, EventPageCtrl eventPageCtrl,
                              UserData userData) {
        super(fullName, null);
        this.id = id;
        this.eventPageCtrl = eventPageCtrl;
        this.userData = userData;
    }
}
