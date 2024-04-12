package client.scenes.javaFXClasses.VisualNode;

import client.UserData;
import client.scenes.EventPageCtrl;
import client.scenes.javaFXClasses.DataNode.ParticipantNode;
import client.utils.ServerUtils;
import commons.DTOs.ParticipantDTO;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import java.util.*;

public class VisualParticipantNode extends ParticipantNode {
    //service variables:
    private boolean editing = false;
    private ParticipantDTO screenshot;

    //create text (shared among all ParticipantNodes)
    Text fNameText = new Text("First Name");
    Text lNameText = new Text("Last Name");
    Text emailText = new Text("Email");
    Text ibanText = new Text("IBAN");
    Text bicText = new Text("BIC");

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
     * creates new javaFX ParticipantNode and fills it with data from ParticipantDTO
     * @param participant data to be used/displayed
     */
    protected VisualParticipantNode(ParticipantDTO participant, EventPageCtrl eventPageCtrl) {
        super(participant.id, participant.getFullName(), eventPageCtrl);
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

        Image img = new Image("/client/Images/edit-button-2.png", 20d, 20d, true, false);


        ImageView imgv = new ImageView(img);
        //create button
        editSaveButton = new Button("Edit",imgv);
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
        deleteButton.setStyle("-fx-text-fill: #ff0000;");
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

        //add behaviour (on close, stop editing)
        this.expandedProperty().addListener((obs, old, expanded) -> {
            if (!expanded) this.cancelEdit();
        });
    }

    /**
     * makes the textFields editable
     * maybe a submit/cancel button appears?
     */
    public void editParticipantFields(ActionEvent actionEvent) {
        System.out.println("editParticipant method called");

        if(editing) { //already edititing -> try to save
            ParticipantDTO p = getParticipantFieldsData();
            try {
                eventPageCtrl.updateParticipant(this, p);
            }  catch (IllegalArgumentException e) {
                return; //continue editing
            }
        } else { //not editing yet -> take screenshot
            screenshot = getParticipantFieldsData();
        }

        //if successful, toggle editing process
        toggleEdit();
    }

    private void toggleEdit() {
        editing = !editing;
        fNameField.setEditable(editing);
        lNameField.setEditable(editing);
        emailField.setEditable(editing);
        ibanField.setEditable(editing);
        bicField.setEditable(editing);

        editSaveButton.setText(editing ? "Save" : "Edit");
    }

    private void cancelEdit() {
        if (!editing) return; //nothing to cancel

        putParticipantFieldsData(screenshot); //restore screenshot
        toggleEdit(); //stop editing
    }

    private void deleteParticipant(ActionEvent actionEvent) {
        if(id==null){
            System.err.println("Error: TransactionNode ID is null.");
            return;
        }
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete event");
            alert.setHeaderText("Do you want to delete this participant?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
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
            }
        } catch (Exception e) {
            System.err.println("Error deleting participant: " + e.getMessage());
        }

    }

    public ParticipantDTO getParticipantFieldsData() {
        String updatedFirstName = fNameField.getText().trim();
        String updatedLastName = lNameField.getText().trim();
        String updatedEmail = emailField.getText().trim();
        String updatedIban = ibanField.getText().trim();
        String updatedBic = bicField.getText().trim();
        UUID eventId = UserData.getInstance().getCurrentUUID();
        return new ParticipantDTO(id, eventId, updatedFirstName,
                updatedLastName, updatedEmail, updatedIban, updatedBic);
    }

    public void putParticipantFieldsData(ParticipantDTO participantDTO) {
        fNameField.setText(participantDTO.firstName);
        lNameField.setText(participantDTO.lastName);
        emailField.setText(participantDTO.email);
        ibanField.setText(participantDTO.iban);
        bicField.setText(participantDTO.bic);
    }
}
