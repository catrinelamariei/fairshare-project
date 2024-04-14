package client.scenes;

import client.*;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.converter.BigDecimalStringConverter;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// BENJAMIN: not checking this because it's not being used -> feel free to merge this into
// eventController>addTransaction
public class TransactionPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Event event;
    @FXML
    private DatePicker dateInput;
    @FXML
    private ChoiceBox currencyCodeInput;
    @FXML
    private TextField amountInput;
    @FXML
    private ChoiceBox authorInput;
    @FXML
    private TextField participantsInpust;
    @FXML
    private TextField tagsInput;
    @FXML
    private TextField subjectInput;
    @FXML
    private VBox participantsContainer;
    @FXML
    private RadioButton equalSplit;
    @FXML
    private RadioButton customSplit;
    @FXML
    private ChoiceBox tagChoices;
    @FXML
    private Button cancel;
    @FXML private Button add;


    ToggleGroup splitTg = new ToggleGroup();


    @Inject
    public TransactionPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;


        // temporary for test
        //todo: delete?
        this.event = new Event("name");
        event.id = new UUID(0, 1);
        Participant p1 = new Participant(event, "fn1", "ln1", "1@gmail.com", "IBAN", "BIC");
        Participant p2 = new Participant(event, "fn2", "ln2", "2@gmail.com", "IBAN", "BIC");
        Participant p3 = new Participant(event, "fn3", "ln3", "2@gmail.com", "IBAN", "BIC");
        event.participants.add(p1);
        event.participants.add(p2);
        event.participants.add(p3);
    }

    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing Transaction Page");

        // add participants to author choices
        for (Participant p : event.participants) {
            authorInput.getItems().add(p.firstName + " " + p.lastName);
            CheckBox c = new CheckBox(p.firstName + " " + p.lastName);
            participantsContainer.getChildren().add(c);
        }

        // add tags to tag choices
        // TODO: tag colors
        for (Tag t : event.tags) {
            tagChoices.getItems().add(t.name);
        }


        // enforces numeric input of form (integer)(.)(1 or 2 d.p.) in amount field
        amountInput.setTextFormatter(new TextFormatter<>(
            new BigDecimalStringConverter(), null, getFilter()));

        // group splitting options
        equalSplit.setToggleGroup(splitTg);
        customSplit.setToggleGroup(splitTg);

        equalSplit.setSelected(true);

        // TODO: ensure if you check customSplit's participant then customSplit is also checked

    }

    private UnaryOperator<TextFormatter.Change> getFilter() {
        return change -> {
            String text = change.getControlNewText();
            if (Pattern.matches("^\\d+(\\.\\d{0,2})?$", text)) {
                return change;
            }
            return null;
        };
    }

    public void gotoHome() {
        mainCtrl.showStartPage();
    }
    public void gotoAdminLogin() {
        mainCtrl.showAdminPage();
    }

    public void createTransaction() {
        String authortxt = (String) authorInput.getValue();
        String subject = subjectInput.getText();
        String amounttxt = amountInput.getText();
        String currencyCode = "eur"; // temp placeholder
        // TODO: tags

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Main.getTranslation("error"));
        alert.setHeaderText(null);



        if (!checkForInvalidValues(authortxt, alert, subject, amounttxt)) {

            Transaction transaction = new Transaction();
            transaction.event = event;

            // convert chosen date to date format
            LocalDate localDate = dateInput.getValue();
            transaction.date = java.sql.Date.valueOf(localDate);
            ;

            transaction.currencyCode = currencyCode;
            BigDecimal amount = new BigDecimal(amountInput.getText());
            transaction.amount = amount;
            Participant author = findParticipant(authortxt);
            transaction.author = author;

            // log the selected participants
            if (equalSplit.isSelected()) transaction.participants = event.participants;
                // TODO: fix customSplit bug
            else if (customSplit.isSelected()) {
                transaction.participants = new HashSet<>();
                var temp = participantsContainer.getChildren()
                    .stream()
                    .map(item -> (CheckBox) item)
                    .filter(item -> item.isSelected())
                    .map(item -> findParticipant(item.getText()))
                    .collect(Collectors.toList());
                for (Participant p : temp) {
                    transaction.participants.add(p);
                }
            }

            transaction.subject = subject;

            // TODO: establish connection
//            server.addTransaction(transaction);
            event.addTransaction(transaction);
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(Main.getTranslation("expense_created"));
            alert.setHeaderText(null);
            alert.setContentText(Main.getTranslation("expense_creation_start")
                    + subject + Main.getTranslation("expense_creation_end"));
            alert.showAndWait();
            eventPage();
        }


    }

    private boolean checkForInvalidValues(String authortxt, Alert alert,
                                          String subject, String amounttxt) {
        boolean participantIsSelected = participantsContainer.getChildren()
            .stream()
            .map(item -> (CheckBox) item)
            .filter(item -> item.isSelected())
            .filter(item -> !item.getText().equals(authortxt))
            .findAny().isPresent();
        boolean hasInvalid = true;
        if (authortxt == null || authortxt.isEmpty()) {
            alert.setContentText(Main.getTranslation("empty_expense_author"));
            alert.showAndWait();
        } else if (subject == null || subject.isEmpty()) {
            alert.setContentText(Main.getTranslation("empty_expense_name"));
            alert.showAndWait();
        } else if (amounttxt == null || amounttxt.isEmpty()) {
            alert.setContentText(Main.getTranslation("empty_expense_amount"));
            alert.showAndWait();
        } else if (dateInput.getValue()==null) {
            alert.setContentText(Main.getTranslation("empty_expense_date"));
            alert.showAndWait();
        } else if (customSplit.isSelected() && !participantIsSelected) {
            alert.setContentText(Main.getTranslation("other_participant"));
            alert.showAndWait();
        } else return false;
        return true;
    }

    private Participant findParticipant(String authortxt) {
        for (Participant p : event.participants) {
            if ((p.firstName.trim() + " " + p.lastName.trim()).equals(authortxt)) return p;
        }
        return null;
    }

    public void eventPage() {
        mainCtrl.showEventPage();
        dateInput.setValue(null);
        subjectInput.clear();
        amountInput.clear();
        currencyCodeInput.getSelectionModel().clearSelection();
        authorInput.getSelectionModel().clearSelection();
        participantsInpust.clear();
        tagsInput.clear();
        equalSplit.setSelected(true);
        participantsContainer.getChildren().forEach(node -> {
            if (node instanceof CheckBox) {
                ((CheckBox) node).setSelected(false);
            }
        });
    }

}
