package client.scenes;

import client.MainCtrl;
import client.UserData;
import client.scenes.javaFXClasses.ParticipantNode;
import client.utils.ServerUtils;
import client.scenes.javaFXClasses.TransactionNode;
import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TagDTO;
import commons.DTOs.TransactionDTO;
import commons.Tag.Color;
import jakarta.ws.rs.WebApplicationException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

import javax.inject.Inject;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javafx.util.Duration;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.List;


public class EventPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    //transaction attributes and buttons
    @FXML
    private VBox transactions;
    @FXML
    private TextField transactionName;
    @FXML
    private TextField transactionAmount;
    @FXML
    private TextField currencyCode;
    @FXML
    private DatePicker transactionDate;

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

    /**
     * This method is NOT done.
     */
    public void onCreateTransaction(){
        String name = transactionName.getText();
        String transactionAmountString = transactionAmount.getText();
        String currency = currencyCode.getText();
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

        // TODO: these should be taken from user input
        ParticipantDTO author = server.postParticipant(new ParticipantDTO(null,
            UserData.getInstance().getCurrentUUID(), "firstName", "lastName", "email@me.com",
            "iban", "bic"));

        Set<ParticipantDTO> participants = new HashSet<>(List.of(server.postParticipant(
            new ParticipantDTO(null, UserData.getInstance().getCurrentUUID(), "firstName",
                "lastName", "email@me.com", "iban", "bic"))));

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
        currencyCode.clear();
        transactionDate.setValue(null);
    }

    public void onAddParticipant() {
        String fName = firstName.getText();
        String lName = lastName.getText();
        String mail = email.getText();
        String ibanText = iban.getText();
        ParticipantDTO participantDTO;

        try {
            if (fName.isEmpty() || lName.isEmpty()|| mail.isEmpty()||ibanText.isEmpty()) {
                throw new IllegalArgumentException();
            }
            participantDTO = new ParticipantDTO(null, UserData.getInstance().getCurrentUUID(),
                fName, lName, mail, ibanText, ""); // TODO: replace empty string with bic
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
    }
}

