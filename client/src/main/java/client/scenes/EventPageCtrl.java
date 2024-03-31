package client.scenes;

import client.MainCtrl;
import client.UserData;
import client.scenes.javaFXClasses.DebtGraph;
import client.scenes.javaFXClasses.DebtNode;
import client.scenes.javaFXClasses.ParticipantNode;
import client.scenes.javaFXClasses.TransactionNode;
import client.utils.ServerUtils;
import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TagDTO;
import commons.DTOs.TransactionDTO;
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
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.util.Duration;
import javafx.util.Pair;

import javax.inject.Inject;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EventPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private EventDTO eventDTO;

    //delete event
    @FXML
    private Button deleteEventButton;

    //delete event
    @FXML
    private Button editButton;

    //event header
    @FXML
    private Text eventTitle;

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

    @FXML
    private VBox debts;
    @FXML
    private Button settleButton;

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
    public void load() throws WebApplicationException {
        System.out.println("loading EventPage");

        eventDTO = server.getEvent(UserData.getInstance().getCurrentUUID());

        //update name
        eventTitle.setText(eventDTO.name);

        //load transactions
        transactions.getChildren().clear();
        transactions.getChildren().addAll(eventDTO.transactions.stream()
            .map(TransactionNode::new).toList());

        //load participants
        participants.getPanes().clear();
        participants.getPanes().addAll(eventDTO.participants.stream().map(ParticipantNode::new)
            .toList());

        //choice box author transaction
        authorInput.setItems(FXCollections.observableArrayList(eventDTO.participants));

        //checkboxes for participants
        vboxParticipantsTransaction.getChildren().setAll(eventDTO.participants.stream()
            .map(EventPageCtrl::participantCheckbox).toList());
        //c1f05a35-1407-4ba1-ada3-0692649256b8
    }

    private static CheckBox participantCheckbox(ParticipantDTO participant) {
        CheckBox checkBox = new CheckBox(participant.toString());
        checkBox.setUserData(participant);
        return checkBox;
    }

    public void gotoHome() {
        mainCtrl.showStartPage();
    }
    public void gotoAdminLogin() {
        mainCtrl.showAdminPage();
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
        Set<TagDTO> tags = new HashSet<>();

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
            if (fName.isEmpty() || lName.isEmpty() || mail.isEmpty()) {
                throw new IllegalArgumentException();
            }
            if (!isValidEmail(mail)) {
                MainCtrl.alert("Please enter a valid email address");
                return;
            }
            if(bicText.isEmpty()){
                bicText="-";
            }
            if(ibanText.isEmpty()){
                ibanText="-";
            }
            participantDTO = new ParticipantDTO(null, UserData.getInstance().getCurrentUUID(),
                fName, lName, mail, ibanText, bicText);
            participantDTO = server.postParticipant(participantDTO);

            //updating event page
            participants.getPanes().add(new ParticipantNode(participantDTO));
            authorInput.getItems().add(participantDTO);
            vboxParticipantsTransaction.getChildren().add(participantCheckbox(participantDTO));
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

    public void debtSimplification() {
        debts.getChildren().clear();
        DebtGraph graph = new DebtGraph(eventDTO);
        PriorityQueue<Pair<ParticipantDTO, Double>> positive = graph.positive;
        PriorityQueue<Pair<ParticipantDTO, Double>> negative = graph.negative;

        // end if no debts to simplify
        if (positive.isEmpty()) {
            debts.getChildren().add(new Text("No debts to simplify"));
            return;
        }

        // display debts if there are debts to simplify
        while (!positive.isEmpty() && !negative.isEmpty()) {

            Pair<ParticipantDTO, Double> pos = positive.poll();
            Pair<ParticipantDTO, Double> neg = negative.poll();

            ParticipantDTO creditor = pos.getKey();
            ParticipantDTO debtor = neg.getKey();
            Double credit = pos.getValue();
            Double debt = neg.getValue();
            double settlementAmount = Math.min(credit, Math.abs(debt));

            // deal with currency later
            DebtNode debtNode = new DebtNode(debtor, creditor, "eur", settlementAmount);
            debts.getChildren().add(debtNode);
            // Update debts
            credit -= settlementAmount;
            debt += settlementAmount;

            // Reinsert participants into priority queues if they still have non-zero debt
            if (debt < 0) {
                negative.offer(new Pair<>(debtor, debt));
            }
            if (credit > 0) {
                positive.offer(new Pair<>(creditor, credit));
            }
        }

        // update the button
        settleButton.setText("Refresh debts");

    }

    public static void printParticipantsSplit(Set<ParticipantDTO> participants){
        for(ParticipantDTO participant : participants){
            System.out.println(participant);
        }
    }
    private boolean isValidEmail(String email) {
        // Regex pattern to match email address
        String regexPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void onDeleteEvent() {
        try {
//            EventDTO event = server.getEvent(UserData.getInstance().getCurrentUUID());
//            UUID eventId = event.getId();
//            server.deleteEvent(eventId);
//            mainCtrl.showStartPage();
            UUID currentUUID = UserData.getInstance().getCurrentUUID();

            server.deleteEvent(currentUUID);
            UserData.getInstance().getRecentUUIDs().removeIf(p -> p.getKey().equals(currentUUID));
            mainCtrl.startPageCtrl.deleteRecentEvent(currentUUID);

            mainCtrl.showStartPage();
            MainCtrl.alert("Event deleted!");
        } catch (WebApplicationException e) {
            System.err.println("Error deleting event: " + e.getMessage());
        }
        //ea8ddca2-0712-4f4a-8410-fe712ab8b86a
        //dd9101e0-5bd1-4df7-bc8c-26d894cb3c71
    }

    public void onEditEvent() {
        TextInputDialog dialog = new TextInputDialog(eventDTO.name);
        dialog.setTitle("Edit Event Name");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the new event name:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(newEventName -> {
            eventDTO.name = newEventName;
            //eventTitle.setText(newEventName);
            try {
                server.putEvent(eventDTO);
                eventTitle.setText(newEventName);
            } catch (WebApplicationException e) {
                System.err.println("Error updating event name: " + e.getMessage());
            }
        });
    }
}

