package client.scenes.javaFXClasses.DataNode;

import commons.DTOs.ParticipantDTO;
import javafx.scene.control.TitledPane;

import java.util.UUID;

public class ParticipantNode extends TitledPane {
    public final UUID id; //to address participant contained in this node
    protected ParticipantNode(UUID id, String fullName) {
        super(fullName, null);
        this.id = id;
    }
}
