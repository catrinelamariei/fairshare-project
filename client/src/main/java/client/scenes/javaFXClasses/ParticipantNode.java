package client.scenes.javaFXClasses;

import client.utils.ServerUtils;
import client.UserData;
import client.scenes.EventPageCtrl;
import commons.DTOs.ParticipantDTO;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;
import java.util.UUID;

public class ParticipantNode extends TitledPane {
    private final UUID id; //to address participant contained in this node
    private final EventPageCtrl eventPageCtrl;
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
    Button editSaveButton;
    Button deleteButton;

    //css styling managed through java
    private static final String textStyle = "-fx-font: bold 20 \"System\"; ";
    private static final String textFieldStyle =
            "-fx-font: italic 16 \"System\"; " +
            "-fx-min-width: 300; ";

    /**
     * initializes styling on shared/static nodes
     */
    public static void init() {
        //apply style to all text
        List.of(fNameText, lNameText, emailText, ibanText, bicText)
                .forEach(t -> t.setStyle(textStyle));
    }

    /**
     * creates new javaFX ParticipantNode and fills it with data from ParticipantDTO
     * @param participant data to be used/displayed
     */
    public ParticipantNode(ParticipantDTO participant, EventPageCtrl eventPageCtrl) {
        super(participant.getFullName(), null);
        this.getStyleClass().add("participants"); //set CSS class
        this.id = participant.id;
        this.eventPageCtrl = eventPageCtrl;

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
        editSaveButton = new Button("Edit");
        editSaveButton.setOnAction(this::editParticipantFields);
        editSaveButton.setFont(Font.font("System", FontWeight.BOLD, 20.0));

        //create pane
        TilePane toggleButtonPane = new TilePane(editSaveButton);
        toggleButtonPane.setAlignment(Pos.CENTER); //child in center
        toggleButtonPane.resize(0d, 0d); //it should shrink

        //delete button
        deleteButton = new Button("Delete");
        deleteButton.setOnAction(this::deleteParticipant);
        deleteButton.setFont(Font.font("System", FontWeight.BOLD, 20.0));
        // Add delete button to the layout
        TilePane deleteButtonPane = new TilePane(deleteButton);
        deleteButtonPane.setAlignment(Pos.CENTER);
        deleteButtonPane.resize(0d, 0d); // It should shrink

        //vbox with the buttons
        VBox buttons = new VBox(10);//toggleButtonPane, deleteButtonPane);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(toggleButtonPane, deleteButtonPane);

        //create HBox, set child
        HBox container = new HBox(gridPane, buttons);
        this.setContent(container);
    }

    /**
     * makes the textFields editable
     * maybe a submit/cancel button appears?
     */
    public void editParticipantFields(ActionEvent actionEvent) {
        System.out.println("editParticipant method called");
        boolean isEditable = !fNameField.isEditable();
        fNameField.setEditable(isEditable);
        lNameField.setEditable(isEditable);
        emailField.setEditable(isEditable);
        ibanField.setEditable(isEditable);
        bicField.setEditable(isEditable);

        if(editSaveButton.getText().equals("Save")) {
            ParticipantDTO p = getUpdatedParticipantData();
            eventPageCtrl.updateParticipant(this, p);
        }
        editSaveButton.setText(isEditable ? "Save" : "Edit");
    }

    private void deleteParticipant(ActionEvent actionEvent) {
        if(id==null){
            System.err.println("Error: TransactionNode ID is null.");
            return;
        }
        try {
            // Delete participant from the server
            ServerUtils serverUtils = new ServerUtils();
            serverUtils.deleteParticipant(id);

            // Remove the participant from the UI
            Node parent = this.getParent();
            if (parent instanceof Accordion) {
                Accordion accordion = (Accordion) parent;
                accordion.getPanes().remove(this);
            }

            System.out.println("Participant deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error deleting participant: " + e.getMessage());
        }

    }

    public ParticipantDTO getUpdatedParticipantData(){
        String updatedFirstName = fNameField.getText().trim();
        String updatedLastName = lNameField.getText().trim();
        String updatedEmail = emailField.getText().trim();
        String updatedIban = ibanField.getText().trim();
        String updatedBic = bicField.getText().trim();
        UUID eventId = UserData.getInstance().getCurrentUUID();
        return new ParticipantDTO(id, eventId, updatedFirstName,
                updatedLastName, updatedEmail, updatedIban, updatedBic);
    }
}
