package client.scenes;

import client.MainCtrl;
import client.UserData;
import client.scenes.javaFXClasses.ParticipantNode;
import client.scenes.javaFXClasses.TransactionNode;
import client.utils.ServerUtils;
import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TagDTO;
import commons.DTOs.TransactionDTO;
import commons.Tag.Color;
import jakarta.ws.rs.WebApplicationException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.Duration;

import javax.inject.Inject;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.*;


public class EventPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    //transaction attributes and buttons
    @FXML
    private TextField transactionName;
    @FXML
    private ChoiceBox<ParticipantDTO> authorInput;
    @FXML
    private TextField transactionAmount;
    @FXML
    private TextField currencyCode;
    @FXML
    private ChoiceBox currencyCodeInput;
    @FXML
    private DatePicker transactionDate;
    @FXML
    private RadioButton equalSplit;
    @FXML
    private RadioButton customSplit;
//    @FXML
//    private Checkbox checkboxParticipant;
    @FXML
    private VBox vboxParticipantsTransaction;
    @FXML
    private ScrollPane participantsScrollPane;
    @FXML
    private TextField tagsInput;

    @FXML private Button add;
    @FXML
    private VBox transactions;
    private ToggleGroup toggleGroup;



    // participant attributes and buttons
    @FXML
    private Accordion participants;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private TextField iban;
    @FXML
    private TextField bic;

    // invite code logic
    @FXML
    private Button copyButton;

    @Inject
    public EventPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize(URL location, ResourceBundle resources) {
        ParticipantNode.init(); //<cascade> do some styling
        currencyCodeInput.getItems().addAll("EUR", "USD", "CHF");
        //radio buttons
        toggleGroup = new ToggleGroup();
        equalSplit.setToggleGroup(toggleGroup);
        customSplit.setToggleGroup(toggleGroup);

        //participants transaction
        vboxParticipantsTransaction = new VBox();
        participantsScrollPane.setContent(vboxParticipantsTransaction);
        vboxParticipantsTransaction.getChildren().clear();

    }
    public void load() {
        System.out.println("loading EventPage");

        try {
            EventDTO event = server.getEvent(UserData.getInstance().getCurrentUUID());

            //load transactions
            transactions.getChildren().clear();
            transactions.getChildren().addAll(event.transactions.stream().map(TransactionNode::new)
                .toList());

            //load participants
            participants.getPanes().clear();
            participants.getPanes().addAll(event.participants.stream().map(ParticipantNode::new)
                .toList());

            //choice box author transaction
            authorInput.setItems(FXCollections.observableArrayList(event.participants));

            //checkboxes for participants
            vboxParticipantsTransaction.getChildren().clear();
            vboxParticipantsTransaction.getChildren().addAll(
                    authorInput.getItems().stream()
                            .map(participant -> {
                                CheckBox checkBox = new CheckBox(participant.toString());
                                checkBox.setUserData(participant);
                                return checkBox;
                            })
                            .toArray(CheckBox[]::new)
            );
            //c1f05a35-1407-4ba1-ada3-0692649256b8

        } catch (WebApplicationException e) {
            System.err.printf("Error while fetching EVENT<%s>: %s%n",
                UserData.getInstance().getCurrentUUID(), e);
        }
    }

    public void gotoHome() {
        mainCtrl.showStartPage();
    }
    public void gotoAdminLogin() {
        mainCtrl.showAdminCheckPage();
    }

    public void copyInviteCode() {

        if(UserData.getInstance().getCurrentUUID() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Event not found");
            alert.showAndWait();
            return;
        }
        //get system singleton of clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        //get invite code (String)
        //copy data to clipboard
        StringSelection content = new StringSelection(
                UserData.getInstance().getCurrentUUID().toString());
        clipboard.setContents(content, null);


        //display copied for 3 seconds
        Label label = new Label("Copied to clipboard!");
        label.setStyle("-fx-font-size: 14px;"); // Set label font size
        StackPane stackPane = new StackPane(label);
        stackPane.setStyle(
                "-fx-background-color: white; -fx-border-color: black;" +
                        " -fx-border-width: 1px; -fx-padding: 2px");


        Popup popup = new Popup();
        popup.getContent().add(stackPane);
        popup.setAutoHide(true);
        Bounds boundsInScreen = copyButton.localToScreen(copyButton.getBoundsInLocal());

        // Position the popup under the button
        double x = boundsInScreen.getMinX();
        double y = boundsInScreen.getMaxY();
        popup.show(copyButton.getScene().getWindow(), x, y);

        final double popupDurationSeconds = 1.5;
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(popupDurationSeconds),
                e -> popup.hide()));
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * placeholder test method for testing the node generator
     */
    public void participantNodeAddTest() {
        ParticipantNode participantNode = new ParticipantNode(new ParticipantDTO(null, null,
                "Max", "Well", "Max.Well@outlook.com", "FR50 1234 5678 9", "KREDBEBB"));
        participants.getPanes().add(participantNode);
    }

    public void toggle(){
        System.out.println("test");
    }

    /**
     * This method is NOT done.
     */
    public void onCreateTransaction(){
        String name = transactionName.getText().trim();
        String transactionAmountString = transactionAmount.getText().trim();
        String currency = (String) currencyCodeInput.getValue();
        LocalDate localDate = transactionDate.getValue();
        BigDecimal amount;

        try {
            if(name==null || transactionAmountString==null || currency==null || localDate==null){
                throw new IllegalArgumentException();
            }
            amount = new BigDecimal(transactionAmountString);
        } catch (NumberFormatException e) {
            MainCtrl.alert("Please enter a number for the Amount field");
            return;
        } catch (IllegalArgumentException e) {
            MainCtrl.alert("Please enter valid transaction information");
            return;
        }

        ParticipantDTO author = authorInput.getValue();

        //join codes for some example transactions
        //c1f05a35-1407-4ba1-ada3-0692649256b8
        //57392209-155d-47fb-9460-3fd3ebca7853

        //radio buttons
        Set<ParticipantDTO> participants = new HashSet<>();

        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if(selectedRadioButton!=null){
            participants = getTransactionParticipants(selectedRadioButton);
        }else{
            MainCtrl.alert("Please chose how to split the transaction!");
            return;
        }

        printParticipantsSplit(participants);

        // TODO: this should be taken from user input
        Set<TagDTO> tags = new HashSet<>(List.of(server.postTag(new TagDTO(null,
                UserData.getInstance().getCurrentUUID(), "newTag", Color.BLUE))));

        Date date = java.sql.Date.valueOf(localDate);
        TransactionDTO ts = new TransactionDTO(null, UserData.getInstance().getCurrentUUID(),
                date, currency, amount, author, participants, tags, name);
        try {
            ts = server.postTransaction(ts);
            transactions.getChildren().add(new TransactionNode(ts));
        } catch (WebApplicationException e) {
            System.err.println("Error creating transaction: " + e.getMessage());
        }

        transactionName.clear();
        transactionAmount.clear();
        authorInput.setValue(null);
        equalSplit.setSelected(false);
        customSplit.setSelected(false);
        currencyCodeInput.setValue(null);
        transactionDate.setValue(null);
        for (Node node : vboxParticipantsTransaction.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                checkBox.setSelected(false);
            }
        }
    }

    private Set<ParticipantDTO> getTransactionParticipants(RadioButton selectedRadioButton) {
        Set<ParticipantDTO> participants = new HashSet<>();
        if (selectedRadioButton == equalSplit) {
            // Handle split equally logic
            ObservableList<ParticipantDTO> choiceBoxItems = authorInput.getItems();
            participants = new HashSet<>(choiceBoxItems);
            System.out.println("Split equally selected");
        } else {
            //Handle custom split
            for (Node node : vboxParticipantsTransaction.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                    if (checkBox.isSelected()) {
                        // Retrieve ParticipantDTO from the checkbox's UserData
                        ParticipantDTO participantDTO = (ParticipantDTO) checkBox.getUserData();
                        if (participantDTO != null) {
                            participants.add(participantDTO);
                        }
                    }
                }
            }
            System.out.println("Split between participants selected");

        }
        return participants;
    }

    public void onAddParticipant() {
        String fName = firstName.getText().trim();
        String lName = lastName.getText().trim();
        String mail = email.getText().trim();
        String ibanText = iban.getText().trim();
        String bicText = bic.getText().trim();
        ParticipantDTO participantDTO;

        try {
            if (fName.isEmpty() || lName.isEmpty() || mail.isEmpty()
                    || ibanText.isEmpty() || bicText.isEmpty()) {
                throw new IllegalArgumentException();
            }
            participantDTO = new ParticipantDTO(null, UserData.getInstance().getCurrentUUID(),
                fName, lName, mail, ibanText, bicText);
            participantDTO = server.postParticipant(participantDTO);
            participants.getPanes().add(new ParticipantNode(participantDTO));
        } catch (IllegalArgumentException e) {
            MainCtrl.alert("Please enter valid participant data");
            return;
        } catch (WebApplicationException e) {
            System.err.println("Error adding participant: " + e.getMessage());
        }

        firstName.clear();
        lastName.clear();
        email.clear();
        iban.clear();
        bic.clear();
    }

    public static void printParticipantsSplit(Set<ParticipantDTO> participants){
        for(ParticipantDTO participant : participants){
            System.out.println(participant);
        }
    }
}

