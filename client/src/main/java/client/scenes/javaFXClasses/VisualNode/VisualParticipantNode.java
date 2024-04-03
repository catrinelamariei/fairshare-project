package client.scenes.javaFXClasses.VisualNode;

import client.scenes.javaFXClasses.DataNode.ParticipantNode;
import commons.DTOs.ParticipantDTO;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;

public class VisualParticipantNode extends ParticipantNode {

    //create text (shared among all ParticipantNodes)
    static Text fNameText = new Text("First Name");
    static Text lNameText = new Text("Last Name");
    static Text emailText = new Text("Email");
    static Text ibanText = new Text("IBAN");
    static Text bicText = new Text("BIC");

    //text-fields are Participant dependant (on data)
    TextField fNameField;
    TextField lNameField;
    TextField emailField;
    TextField ibanField;
    TextField bicField;
    Button toggleEditButton;

    //css styling managed through java
    private static final String textStyle = "-fx-font: bold 20 \"System\"; ";
    private static final String textFieldStyle =
            "-fx-font: italic 16 \"System\"; " +
            "-fx-min-width: 300; ";

    /**
     * creates new javaFX ParticipantNode and fills it with data from ParticipantDTO
     * @param participant data to be used/displayed
     */
    protected VisualParticipantNode(ParticipantDTO participant) {
        super(participant.id, participant.getFullName());
        this.getStyleClass().add("participants"); //set CSS class

        //apply style to all text
        List.of(fNameText, lNameText, emailText, ibanText, bicText)
                .forEach(t -> t.setStyle(textStyle));

        //create text-field
        fNameField = new TextField(participant.firstName);
        lNameField = new TextField(participant.lastName);
        emailField = new TextField(participant.email);
        ibanField = new TextField(participant.iban);
        bicField = new TextField(participant.bic);
        //apply style
        List.of(fNameField, lNameField, emailField, ibanField, bicField)
                .forEach(tf -> {
                    tf.setStyle(textFieldStyle);
                    tf.setEditable(false);
                });

        //create table
        GridPane gridPane = new GridPane();
        gridPane.add(fNameText, 0, 0);
        gridPane.add(lNameText, 0, 1);
        gridPane.add(emailText, 0, 2);
        gridPane.add(ibanText, 0, 3);
        gridPane.add(bicText, 0, 4);

        gridPane.add(fNameField, 1, 0);
        gridPane.add(lNameField, 1, 1);
        gridPane.add(emailField, 1, 2);
        gridPane.add(ibanField, 1, 3);
        gridPane.add(bicField, 1, 4);

        //set insets
        Insets insets = new Insets(10.0d);
        gridPane.getChildren().forEach(n -> gridPane.setMargin(n, insets));

        //set HGrow
        ColumnConstraints col0 = new ColumnConstraints();
        ColumnConstraints col1  = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(col0, col1);

        //create button
        toggleEditButton = new Button("toggle edit");
        toggleEditButton.setOnAction(this::toggleEdit);
        toggleEditButton.setFont(Font.font("System", FontWeight.BOLD, 20.0));

        //create pane
        TilePane filler = new TilePane(toggleEditButton);
        filler.setAlignment(Pos.CENTER); //child in center
        filler.resize(0d, 0d); //it should shrink

        //create HBox, set child
        HBox container = new HBox(gridPane, filler);
        this.setContent(container);
    }

    /**
     * makes the textFields editable
     * maybe a submit/cancel button appears?
     */
    private void toggleEdit(ActionEvent actionEvent) {
        System.out.println("Started Editing");
    }
}
