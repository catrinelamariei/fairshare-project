package client.scenes.javaFXClasses;

import commons.DTOs.ParticipantDTO;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ParticipantNode extends TitledPane {
    //create text (shared among all ParticipantNodes)
    static Text nameText = new Text("name");
    static Text emailText = new Text("Email");
    static Text ibanText = new Text("IBAN");
    static Text bicText = new Text("BIC");
    //text-fields are Participant dependant (on data)
    TextField nameField;
    TextField emailField;
    TextField ibanField;
    TextField bicField;
    Button toggleEditButton;

    /**
     * creates new javaFX ParticipantNode and fills it with data from ParticipantDTO
     * @param participant data to be used/displayed
     */
    public ParticipantNode(ParticipantDTO participant) {
        super(participant.getFullName(), null);
        this.getStyleClass().add("participants"); //set CSS class
        //create text-field
        nameField = new TextField(participant.getFullName());
        emailField = new TextField(participant.email);
        ibanField = new TextField(participant.iban);
        bicField = new TextField("???");

        //create table
        GridPane gridPane = new GridPane();
        gridPane.add(nameText, 0, 0);
        gridPane.add(emailText, 0, 1);
        gridPane.add(ibanText, 0, 2);
        gridPane.add(bicText, 0, 3);
        gridPane.add(nameField, 1, 0);
        gridPane.add(emailField, 1, 1);
        gridPane.add(ibanField, 1, 2);
        gridPane.add(bicField, 1, 3);

        //create button
        toggleEditButton = new Button("toggle edit");
        toggleEditButton.setOnAction(this::toggleEdit);

        //create pane
        Pane filler = new Pane(toggleEditButton);

        //create HBox, set child
        HBox container = new HBox(gridPane, filler);
        this.setContent(container);
        System.out.printf("TITLEDPANE: %b, %b\n", this.collapsibleProperty(), this.expandedProperty());
    }

    /**
     * makes the textFields editable
     * maybe a submit/cancel button appears?
     */
    private void toggleEdit(ActionEvent actionEvent) {

    }
}
