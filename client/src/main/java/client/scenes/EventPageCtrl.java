package client.scenes;

import client.*;
import client.scenes.javaFXClasses.DataNode.*;
import client.scenes.javaFXClasses.NodeFactory;
import client.utils.*;
import com.google.inject.*;
import commons.DTOs.*;
import jakarta.ws.rs.WebApplicationException;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.util.Duration;
import javafx.util.*;

import java.awt.*;
import java.awt.datatransfer.*;
import java.math.BigDecimal;
import java.net.URL;
import java.time.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

import static client.utils.UndoService.TsAction.*;

@Singleton //for provider
public class EventPageCtrl implements Initializable {
    //Services
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final NodeFactory nodeFactory;
    public final UndoService undoService; //should be made private using factory injection

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
    private Tab addExpenseTab;
    @FXML
    private Tab expenseOverviewTab;
    @FXML
    private TextField transactionName;
    @FXML
    private ChoiceBox<ParticipantDTO> authorInput;
    @FXML
    private TextField transactionAmount;
    @FXML
    private TextField currencyCode;
    @FXML
    private ChoiceBox<String> currencyCodeInput;
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
    private ComboBox<TagDTO> tagsInput;
    @FXML
    private Button submitTransaction;
    @FXML
    private Button cancelTransaction;
    @FXML
    public VBox transactions;
    @FXML
    private VBox tagsVBox;
    private ToggleGroup toggleGroup;
    private TransactionNode transactionEditTarget;

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
    public Accordion debts;
    @FXML
    private Button settleButton;
    @FXML
    private ChoiceBox creditorFilter;

    @FXML
    private TabPane participantTabPane;
    @FXML
    Text totalExpenses;
    @FXML
    private Button statsButton;

    @FXML
    private TabPane expenseTabPane;

    @FXML
    private Tab overviewExpenses;

    @FXML
    private Tab overviewParticipants;

    //Statistics
    @FXML
    private PieChart pieChart;

    @FXML
    private Button updateChart;
    @FXML
    private Text eventCost;

    Set<TagDTO> tags = new HashSet<>();

    @FXML
    private ChoiceBox<String> payerFilter;
    @FXML
    private ChoiceBox<String> participantFilter;

    @Inject
    public EventPageCtrl(ServerUtils server, MainCtrl mainCtrl, UndoService undoService,
                         NodeFactory nodeFactory) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.undoService = undoService;
        this.nodeFactory = nodeFactory;
    }

    public void initialize(URL location, ResourceBundle resources) {
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
        server.registerForUpdatesTransaction(t ->{
            Platform.runLater(()->{
                eventDTO = server.getEvent(UserData.getInstance().getCurrentUUID());
                transactions.getChildren().clear();
                transactions.getChildren().addAll(eventDTO.transactions.stream()
                        .map(nodeFactory::createTransactionNode).toList());
            });
        });

        subscribe();

    }

    private void subscribe() {
        server.registerForUpdatesParticipant(p->{
            Platform.runLater(()->{
                if(p.eventId.equals(UserData.getInstance().getCurrentUUID())){
                    participants.getPanes().clear();
                    eventDTO.participants.removeIf(participantDTO ->
                            participantDTO.getId().equals(p.getId()));
                    eventDTO.participants.add(p);
                    participants.getPanes().addAll(eventDTO.participants.stream()
                            .map(nodeFactory::createParticipantNode).toList());
                    authorInput.getItems().clear();
                    authorInput.getItems().addAll(eventDTO.participants);
                    vboxParticipantsTransaction.getChildren().clear();
                    vboxParticipantsTransaction.getChildren().addAll(eventDTO.participants.stream()
                            .map(EventPageCtrl::participantCheckbox).toList());
                    payerFilter.getItems().clear();
                    participantFilter.getItems().clear();
                    payerFilter.getItems().add("All");
                    participantFilter.getItems().add("All");
                    for (ParticipantDTO part : eventDTO.participants) {
                        payerFilter.getItems().add(part.getFullName());
                        participantFilter.getItems().add(part.getFullName());
                    }
                    payerFilter.setValue("All");
                    participantFilter.setValue("All");
                }


            });
        });

        server.registerForParticipantDeletionUpdates(id->{
            Platform.runLater(()->{
                if(eventDTO.participants.stream().anyMatch(p->p.getId().equals(id))){
                    eventDTO.participants.removeIf(p->p.getId().equals(id));
                    participants.getPanes().clear();
                    participants.getPanes().addAll(eventDTO.participants.stream()
                            .map(nodeFactory::createParticipantNode).toList());
                    authorInput.getItems().clear();
                    authorInput.getItems().addAll(eventDTO.participants);
                    vboxParticipantsTransaction.getChildren().clear();
                    vboxParticipantsTransaction.getChildren().addAll(eventDTO.participants.stream()
                            .map(EventPageCtrl::participantCheckbox).toList());
                    payerFilter.getItems().clear();
                    participantFilter.getItems().clear();
                    payerFilter.getItems().add("All");
                    participantFilter.getItems().add("All");
                    for (ParticipantDTO part : eventDTO.participants) {
                        payerFilter.getItems().add(part.getFullName());
                        participantFilter.getItems().add(part.getFullName());
                    }
                    payerFilter.setValue("All");
                    participantFilter.setValue("All");


                    //this is necessary because sometimes, deleting
                    // a participant will also delete a transaction
                    EventDTO e = server.getEvent(UserData.getInstance().getCurrentUUID());
                    transactions.getChildren().clear();
                    transactions.getChildren().addAll(e.transactions.stream()
                            .map(nodeFactory::createTransactionNode).toList());



                }
            });
        });


    }

    private void loadTransactions(){
        transactions.getChildren().clear();
        transactions.getChildren().addAll(eventDTO.transactions.stream()
                .map(nodeFactory::createTransactionNode).toList());


        //loading stats
        updateChart.setText("Load Statistics");
        updateChart.setOnAction(e -> {
            updateChart.setText("Refresh statistics");
            loadPieChart();
            eventCost.setText("\u20AC " + printTotalExpenses());
            updateTotalExpenses();
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
                .map(nodeFactory::createTransactionNode).toList());

        //load participants
        participants.getPanes().clear();
        participants.getPanes().addAll(eventDTO.participants.stream()
                .map(nodeFactory::createParticipantNode).toList());

        //choice box author transaction
        authorInput.setItems(FXCollections.observableArrayList(eventDTO.participants));

        //checkboxes for participants
        vboxParticipantsTransaction.getChildren().setAll(eventDTO.participants.stream()
                .map(EventPageCtrl::participantCheckbox).toList());

        //choiceboxes for debt filter
        creditorFilter.getItems().clear();
        creditorFilter.getItems().add("All");
        for (ParticipantDTO participant : eventDTO.participants) {
            creditorFilter.getItems().add(participant.getFullName());
        }
        creditorFilter.setValue("All");

        // debt
        debts.getPanes().clear();
        settleButton.setText("Settle debts");
        //c1f05a35-1407-4ba1-ada3-0692649256b8

        //choiceboxes for transaction filter
        payerFilter.getItems().clear();
        participantFilter.getItems().clear();
        payerFilter.getItems().add("All");
        participantFilter.getItems().add("All");
        for (ParticipantDTO p : eventDTO.participants) {
            payerFilter.getItems().add(p.getFullName());
            participantFilter.getItems().add(p.getFullName());
        }
        payerFilter.setValue("All");
        participantFilter.setValue("All");

        //tags
        tagsInput.getItems().setAll(eventDTO.tags.stream().toList());
        tagsInput.setCellFactory(new Callback<ListView<TagDTO>, ListCell<TagDTO>>() {
            @Override
            public ListCell<TagDTO> call(ListView<TagDTO> param) {
                return new ListCell<TagDTO>() {
                    @Override
                    protected void updateItem(TagDTO item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName());
                            setStyle("-fx-background-color: " + item.color.colorCode + ";");
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
        undoService.clear();
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
        tagBox.setStyle("-fx-background-color: " + input.color.colorCode + ";");
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

        if (UserData.getInstance().getCurrentUUID() == null) {
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

    public void onCreateTransaction(ActionEvent event) {
        TransactionDTO ts = readTransactionFields();

        if (createTransaction(ts) != null)
            MainCtrl.inform("Expense","Expense \"" + ts.getSubject() + "\" Created!");

    }

    public TransactionDTO createTransaction(TransactionDTO ts) {
        if (ts == null) return null;

        try {
            ts = server.postTransaction(ts);
            undoService.addAction(CREATE, ts);
            String selectedPayer = payerFilter.getValue();
            String selectedParticipant = participantFilter.getValue();
            if ((selectedPayer.equals("All")
                    || selectedPayer.equals(ts.author.getFullName()))
                    && (selectedParticipant.equals("All") ||
                    ts.participants.stream().anyMatch(p ->
                            p.getFullName().equals(selectedParticipant)))) {

                TransactionNode tsNode = nodeFactory.createTransactionNode(ts);
                transactions.getChildren().add(tsNode);
            }
        } catch (WebApplicationException e) {
            System.err.println("Error creating expense: " + e.getMessage());
            return null;
        }

        clearTransaction();
        showOverviewTransactions();
        return ts;
    }


    public void updateTotalExpenses() {
        EventDTO e = server.getEvent(UserData.getInstance().getCurrentUUID());
        totalExpenses.setText("\u20AC" +
                String.valueOf(e.getTransactions().stream()
                .filter(
                        ts -> ts.getTags()
                                .stream()
                                .map(tag -> tag.getName())
                                .noneMatch(tagName -> tagName.equals("debt")))
                .mapToDouble(ts -> ts.getAmount().doubleValue())
                .sum()));
        System.out.println(totalExpenses.getText());
    }

    private TransactionDTO readTransactionFields() {
        String name = transactionName.getText().trim();
        String transactionAmountString = transactionAmount.getText().trim();
        String currency = currencyCodeInput.getValue();
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

        if (!infoIsValid(name, author, amount, currency, localDate, selectedRadioButton,
                participantIsSelected))
            return null;

        participants = getTransactionParticipants(selectedRadioButton);


        Date date = java.sql.Date.valueOf(localDate);
        return new TransactionDTO(null, UserData.getInstance().getCurrentUUID(),
                date, currency, amount, author, participants, tags, name);
    }

    public void clearTransaction() {
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

        //return buttons/fields to default functions/value
        submitTransaction.setOnAction(this::onCreateTransaction);
        addExpenseTab.setText("Add Expense");
        vboxParticipantsTransaction.getChildren().forEach(node -> {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                checkBox.setDisable(false);
                checkBox.setSelected(false);
            }
        });
    }


    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    private boolean infoIsValid(String name, ParticipantDTO author, BigDecimal amount,
                                String currency, LocalDate localDate,
                                RadioButton selectedRadioButton,
                                boolean participantIsSelected) {
        if (name == null || name.isEmpty()) {
            MainCtrl.alert("Please enter a description.");
        } else if (author == null) {
            MainCtrl.alert("Please select a payer.");
        } else if (amount == null) {
            MainCtrl.alert("Please enter a valid amount.");
        } else if (currency == null || currency.isEmpty()) {
            MainCtrl.alert("Please choose a currency code.");
        } else if (localDate == null) {
            MainCtrl.alert("Please choose a date.");
        } else if (selectedRadioButton == null) {
            MainCtrl.alert("Please select an option for splitting the expense.");
        } else if (customSplit.isSelected() && !participantIsSelected) {
            MainCtrl.alert("Please choose at least one participant other than yourself.");
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

    private boolean checkInput(String name, String transactionAmountString, String currency,
                               LocalDate localDate, ParticipantDTO author) {
        if (name == null || name.isEmpty()) {
            MainCtrl.alert("Please enter the name of the expense");
            return true;
        }
        if (author == null) {
            MainCtrl.alert("Please chose the author of the transaction");
            return true;
        }
        if (transactionAmountString == null || transactionAmountString.isEmpty()) {
            MainCtrl.alert("Please enter the amount of the expense");
            return true;
        }
        if (currency == null) {
            MainCtrl.alert("Please enter the currency of the expense");
            return true;
        }
        if (localDate == null) {
            MainCtrl.alert("Please enter the date of the expense");
            return true;
        }

        return false;
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
                if (node instanceof CheckBox checkBox) {
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
            if (fName.isEmpty()) {
                MainCtrl.alert("Please enter the first name");
                return;
            }
            if (lName.isEmpty()) {
                MainCtrl.alert("Please enter the last name");
                return;
            }
            if (mail.isEmpty() || invalidEmail(mail)) {
                MainCtrl.alert("Please enter a valid email address");
                return;
            }
            if (bicText.isEmpty()) {
                bicText = "-";
            }
            if (ibanText.isEmpty()) {
                ibanText = "-";
            }
            participantDTO = new ParticipantDTO(null, UserData.getInstance().getCurrentUUID(),
                    fName, lName, mail, ibanText, bicText);
            participantDTO = server.postParticipant(participantDTO);

            //updating event page
            participants.getPanes().add(nodeFactory.createParticipantNode(participantDTO));
            authorInput.getItems().add(participantDTO);
            vboxParticipantsTransaction.getChildren().add(participantCheckbox(participantDTO));
            payerFilter.getItems().add(participantDTO.getFullName());
            participantFilter.getItems().add(participantDTO.getFullName());
            creditorFilter.getItems().add(participantDTO.getFullName());
            showOverviewParticipants();
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

    @FXML
    public void debtSimplification() {

        debts.getPanes().clear();

        EventDTO event = server.getEvent(UserData.getInstance().getCurrentUUID());

        DebtGraph graph = new DebtGraph(event);
        PriorityQueue<Pair<ParticipantDTO, Double>> positive = graph.positive;
        PriorityQueue<Pair<ParticipantDTO, Double>> negative = graph.negative;

        // end if no debts to simplify
        if (positive.isEmpty()) {
            MainCtrl.inform("Debts","No debts to simplify!");
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
            DebtNode debtNode = nodeFactory.createDebtNode(debtor, creditor, "eur",
                    settlementAmount, event, server, this);
            debts.getPanes().add(debtNode);
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
        filterDebts();

    }


    public void filterDebts() {
        String selectedCreditor = (String) creditorFilter.getValue();
        // remove other debtNodes if a creditor is selected
        Set<TitledPane> toRemove = new HashSet<>() ;
        if (!selectedCreditor.equals("All")) {
            debts.getPanes().forEach(debtNode -> {
                DebtNode node = (DebtNode) debtNode;
                if (!node.creditor.getFullName().equals(selectedCreditor)) {
                    toRemove.add(node);
                }
            });
        }
        debts.getPanes().removeAll(toRemove);
    }

    private boolean invalidEmail(String email) {
        // Regex pattern to match email address
        String regexPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
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

    public void showOverviewParticipants() {
        participantTabPane.getSelectionModel().select(overviewParticipants);
    }

    public void showOverviewTransactions() {
        expenseTabPane.getSelectionModel().select(overviewExpenses);
    }

    public void enableEditing(TransactionNode tsn) {
        transactionEditTarget = tsn;
        addExpenseTab.setText("Edit Expense");
        addExpenseTab.getTabPane().getSelectionModel().select(addExpenseTab);
        submitTransaction.setOnAction(this::submitEditTransaction);
    }

    @FXML
    public void filterTransactions() {
        String selectedPayer = (String) payerFilter.getValue();
        String selectedParticipant = (String) participantFilter.getValue();
        EventDTO event = server.getEvent(UserData.getInstance().getCurrentUUID());

        transactions.getChildren().setAll(
                event.transactions
                        .stream()
                        .filter(ts -> selectedPayer.equals("All")
                                || ts.author.getFullName().equals(selectedPayer))
                        .filter(ts -> selectedParticipant.equals("All") ||
                                ts.participants.stream()
                                        .anyMatch(p -> p.getFullName().equals(selectedParticipant)))
                        .map(t -> nodeFactory.createTransactionNode(t))
                        .collect(Collectors.toList()));
    }

    public void fillTransaction(TransactionDTO transaction) {
        this.transactionName.setText(transaction.getSubject());
        this.transactionAmount.setText(transaction.getAmount().toString());
        this.currencyCodeInput.setValue(transaction.getCurrencyCode());
        this.transactionDate.setValue(transaction.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate());
        this.authorInput.setValue(transaction.getAuthor());
        this.toggleGroup.selectToggle(customSplit);

        //select checkboxes of participants
        vboxParticipantsTransaction.getChildren().stream().filter(CheckBox.class::isInstance)
                .map(CheckBox.class::cast)
                .filter(cb -> transaction.getParticipants().contains(cb.getUserData()))
                .forEach(cb -> cb.setSelected(true));
    }

    //TODO do we need this method maybe we can use only updateTransaction?
    public void submitEditTransaction(ActionEvent event) {
        TransactionDTO ts = readTransactionFields();

        updateTransaction(ts);
    }

    private void updateTransaction(TransactionDTO ts) {
        if (ts == null) return;
        if (transactionEditTarget == null) {
            MainCtrl.alert("ERROR: no transaction target set");
            return;
        }

        //updating DB and local list
        ts.id = transactionEditTarget.id;
        TransactionDTO old = server.getTransaction(ts.id);
        undoService.addAction(UPDATE, old);
        TransactionNode updatedTSNode = nodeFactory
                .createTransactionNode(server.putTransaction(ts));
        int index = this.transactions.getChildren().indexOf(transactionEditTarget);
        this.transactions.getChildren().set(index, updatedTSNode);
        clearTransaction();
        addExpenseTab.getTabPane().getSelectionModel().select(expenseOverviewTab);
    }


    public void updateParticipant(ParticipantNode oldNode, ParticipantDTO newParticipant) {
        if (newParticipant == null) {
            return;
        }

        try {
            if (newParticipant.getFirstName().isEmpty() || newParticipant.getLastName().isEmpty()
                    || newParticipant.getEmail().isEmpty()) {
                throw new IllegalArgumentException();
            }
            if (invalidEmail(newParticipant.getEmail())) {
                MainCtrl.alert("Please enter a valid email address");
                return;
            }
            if (newParticipant.getBic().isEmpty()) {
                newParticipant.setBic("-");
            }
            if (newParticipant.getIban().isEmpty()) {
                newParticipant.setIban("-");
            }

            int nodeIndex = participants.getPanes().indexOf(oldNode);
            participants.getPanes().set(nodeIndex,
                    nodeFactory.createParticipantNode(newParticipant));
            server.putParticipant(newParticipant);
            load();

        } catch (IllegalArgumentException e) {
            MainCtrl.alert("Please enter valid participant data");
        } catch (WebApplicationException e) {
            System.err.println("Error updating participant: " + e.getMessage());
        }
    }

    public void loadPieChart() {

        Map<String, BigDecimal> tagToAmount = new HashMap<>();
        EventDTO event = server.getEvent(UserData.getInstance().getCurrentUUID());
        Set<TransactionDTO> transactions = event.getTransactions();
        for(TransactionDTO t : transactions){
            Set<TagDTO> tags = t.getTags();
            for(TagDTO tag : tags){
                String tagName = tag.getName();
                BigDecimal amount = t.getAmount();
                tagToAmount.put(tagName, amount);
            }
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                tagToAmount.entrySet().stream()
                        .map(e -> new PieChart.Data(e.getKey(), e.getValue().doubleValue()))
                        .collect(Collectors.toList())
        );

        pieChart.setData(pieData);
    }

    public String printTotalExpenses() {
        EventDTO event = server.getEvent(UserData.getInstance().getCurrentUUID());
        Set<TransactionDTO> transactions = event.getTransactions();
        double totalExpenses = transactions.stream()
                .map(TransactionDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();
        return (String.valueOf(totalExpenses));
    }

    public void stop(){
        server.stop();
    }

}