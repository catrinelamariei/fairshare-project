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
import javafx.scene.layout.*;
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
    private ChoiceBox<TagDTO> tagsInput;

    @FXML private Button add;
    @FXML
    private VBox transactions;
    @FXML
    private VBox tagsVBox;
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


    Set<TagDTO> tags = new HashSet<>();

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

        //splitting interactivity
        equalSplit.setOnAction(e -> {
            vboxParticipantsTransaction.getChildren().forEach(node -> {
                if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                    checkBox.setDisable(true);
                    checkBox.setSelected(true);
                }
            });
        });
        customSplit.setOnAction(e -> {
            vboxParticipantsTransaction.getChildren().forEach(node -> {
                if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                    checkBox.setDisable(false);
                    checkBox.setSelected(false);
                }
            });
        });
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

        //tags
        tagsInput.getItems().setAll(eventDTO.tags.stream().toList());

    }

    private static CheckBox participantCheckbox(ParticipantDTO participant) {
        CheckBox checkBox = new CheckBox(participant.toString());
        checkBox.setUserData(participant);
        return checkBox;
    }


    @FXML
    private void addTag() {
        TagDTO input = tagsInput.getValue();
        if (input == null) {
            MainCtrl.alert("Please choose a tag from the dropdown menu");
            return;
        } else if (tags.contains(input)) {
            MainCtrl.alert("Tag already added");
            return;
        }
        tagsInput.setValue(null);

        HBox tagBox = new HBox();
        Button deleteTag = new Button("X");
        deleteTag.setOnAction(e2 -> {
            tags.remove(input);
            tagsVBox.getChildren().remove(tagBox);
        });
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        tagBox.getChildren().add(new Text(input.getName()));
        tagBox.getChildren().add(spacer);
        tagBox.getChildren().add(deleteTag);
        tagsVBox.getChildren().add(tagBox);
        tags.add(input);
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


    public void toggle(){
        System.out.println("test");
    }


    public void onCreateTransaction(){
        String name = transactionName.getText().trim();
        String transactionAmountString = transactionAmount.getText().trim();
        String currency = (String) currencyCodeInput.getValue();
        LocalDate localDate = transactionDate.getValue();
        BigDecimal amount;
        ParticipantDTO author = authorInput.getValue();
        //radio buttons
        Set<ParticipantDTO> participants;
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();


        boolean participantIsSelected = vboxParticipantsTransaction.getChildren()
            .stream()
            .map(item -> (CheckBox) item)
            .filter(item -> item.isSelected())
            .filter(item -> !item.getText().equals(author.toString()))
            .findAny().isPresent();
        amount = isValidAmount(transactionAmountString);
        boolean authorIsSelected = vboxParticipantsTransaction.getChildren()
            .stream()
            .map(item -> (CheckBox) item)
            .filter(item -> item.isSelected())
            .filter(item -> item.getText().equals(author.toString()))
            .findAny().isPresent();

        if (!infoIsValid(name, author, amount, currency, localDate, selectedRadioButton,
            participantIsSelected, authorIsSelected)) return;

        participants = getTransactionParticipants(selectedRadioButton);

        //join codes for some example transactions
        //c1f05a35-1407-4ba1-ada3-0692649256b8
        //57392209-155d-47fb-9460-3fd3ebca7853

        Date date = java.sql.Date.valueOf(localDate);
        TransactionDTO ts = new TransactionDTO(null, UserData.getInstance().getCurrentUUID(),
                date, currency, amount, author, participants, tags, name);
        try {
            ts = server.postTransaction(ts);
            transactions.getChildren().add(new TransactionNode(ts));
        } catch (WebApplicationException e) {
            System.err.println("Error creating transaction: " + e.getMessage());
        }

        clearTransactionFields();
        MainCtrl.inform("Transaction created successfully");
    }

    @FXML
    private void clearTransactionFields() {
        transactionName.clear();
        transactionAmount.clear();
        authorInput.setValue(null);
        equalSplit.setSelected(false);
        customSplit.setSelected(false);
        currencyCodeInput.setValue(null);
        transactionDate.setValue(null);
        tagsInput.setValue(null);
        tags.clear();
        tagsVBox.getChildren().clear();
        for (Node node : vboxParticipantsTransaction.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                checkBox.setSelected(false);
            }
        }
    }


    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    private boolean infoIsValid(String name, ParticipantDTO author, BigDecimal amount,
                                String currency, LocalDate localDate,
                                RadioButton selectedRadioButton,
                                boolean participantIsSelected, boolean authorIsSelected) {
        if (name == null || name.isEmpty()) {
            MainCtrl.alert("Please enter a description");
        } else if (author == null) {
            MainCtrl.alert("Please select a payer");
        }  else if (amount == null) {
            MainCtrl.alert("Please enter a valid amount");
        } else if (currency == null || currency.isEmpty()) {
            MainCtrl.alert("Please choose a currency code");
        } else if (localDate ==null) {
            MainCtrl.alert("Date cannot be empty");
        } else if(selectedRadioButton ==null){
            MainCtrl.alert("Please chose how to split the transaction!");
        } else if (customSplit.isSelected()) {
            if (!participantIsSelected) {
                MainCtrl.alert("Select at least 1 participant that isn't the author");
            } else if (!authorIsSelected) {
                MainCtrl.alert("Select the author as a participant");
            }
        } else {
            return true;
        }
        return false;
    }

    private static BigDecimal isValidAmount(String transactionAmountString) {
        BigDecimal amount;
        try {
            amount = new BigDecimal(transactionAmountString);
        } catch (NumberFormatException e) {
            return null;
        }
        return amount;
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
}

