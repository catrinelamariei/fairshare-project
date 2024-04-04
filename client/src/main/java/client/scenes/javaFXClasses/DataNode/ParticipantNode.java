package client.scenes.javaFXClasses.DataNode;

import client.scenes.EventPageCtrl;
import javafx.scene.control.TitledPane;

import java.util.UUID;

public class ParticipantNode extends TitledPane {
    public final UUID id; //to address participant contained in this node
    protected final EventPageCtrl eventPageCtrl;

    protected ParticipantNode(UUID id, String fullName, EventPageCtrl eventPageCtrl) {
        super(fullName, null);
        this.id = id;
        this.eventPageCtrl = eventPageCtrl;
    }
}
