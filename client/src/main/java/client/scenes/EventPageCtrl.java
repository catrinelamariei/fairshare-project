package client.scenes;

import client.MainCtrl;
import client.UserData;
import client.scenes.javaFXClasses.ParticipantNode;
import client.utils.ServerUtils;
import client.scenes.javaFXClasses.TransactionNode;
import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TransactionDTO;
import jakarta.ws.rs.WebApplicationException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;

import javax.inject.Inject;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

import java.util.stream.Collectors;

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

    //participant attributes and buttons
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
            for (TransactionDTO ts : event.transactions) {
                transactions.getChildren().add(new TransactionNode(ts));
            }

            //load participants
            for (ParticipantDTO p : event.participants) {
                participants.getPanes().add(new ParticipantNode(p));
            }

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
        StringSelection content = new StringSelection(UserData.getInstance().getCurrentUUID().toString());
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

    public void onNewTransaction() {
        mainCtrl.showTransactionPage();
    }

    /**
     * placeholder test method for testing the node generator
     */
    public void participantNodeAddTest() {
        ParticipantNode participantNode = new ParticipantNode(new ParticipantDTO(
                "Max", "Well", "Max.Well@outlook.com", "FR50 1234 5678 9"
        ));
        participants.getPanes().add(participantNode);
    }

    /**
     * This method is NOT done.
     */
    public void onCreateTransaction(){
        String name = transactionName.getText();
        String transactionAmountString = transactionAmount.getText();
        String currency = currencyCode.getText();
        LocalDate date = transactionDate.getValue();
        if(name==null || transactionAmountString==null || currency==null || date==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid transaction information");
            alert.showAndWait();
            return;
        }
        try {

            BigDecimal amount = new BigDecimal(transactionAmountString);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a number for the Amount field");
            alert.showAndWait();
            return;
        }

        BigDecimal amount = new BigDecimal(transactionAmountString);
        Date d = java.sql.Date.valueOf(date);
        UUID uuid = UUID.randomUUID();
        TransactionDTO newTransactionDTO = new TransactionDTO(
                uuid, d, currency, amount, null, name);
        try {
            transactionName.clear();
            transactionAmount.clear();
            currencyCode.clear();
            transactionDate.setValue(null);
            EventDTO eventDTO = server.getEvent(UserData.getInstance().getCurrentUUID());
            eventDTO.transactions.add(newTransactionDTO);
            server.updateEvent(eventDTO);
            HBox transactionNode = new TransactionNode(newTransactionDTO);
            transactions.getChildren().add(transactionNode);

        } catch (WebApplicationException e) {
            System.err.println("Error creating transaction: " + e.getMessage());
        }
    }

    public void onAddParticipant() {
        String fName = firstName.getText();
        String lName = lastName.getText();
        String mail = email.getText();
        String ibanText = iban.getText();
        if (fName.isEmpty() || lName.isEmpty()|| mail.isEmpty()||ibanText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid participant data");
            alert.showAndWait();
            return;
        }
        try {
            ParticipantDTO participantDTO = new ParticipantDTO(fName, lName, mail, ibanText);
            EventDTO eventDTO = server.getEvent(UserData.getInstance().getCurrentUUID());
            eventDTO.participants.add(participantDTO);
            server.updateEvent(eventDTO);
        } catch (WebApplicationException e) {
            System.err.println("Error adding participant: " + e.getMessage());
        }
        firstName.clear();
        lastName.clear();
        email.clear();
        iban.clear();
    }


}

