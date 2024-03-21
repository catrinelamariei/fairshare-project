package client.scenes;

import client.MainCtrl;
import client.UserData;
import client.scenes.javaFXClasses.DebtNode;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.Duration;
import javafx.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

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

    @FXML
    private VBox debts;

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

    public void debtSimplification() {
        EventDTO event = server.getEvent(UserData.getInstance().getCurrentUUID());

        Graph<ParticipantDTO, DefaultWeightedEdge> graph =
            new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);


        populateGraph(event, graph);

        // get total amount each participant owes/is owed
        // put them in two priority queues,
        // 1 for if they owe money,
        // 1 for if they are owed money


        // pq with descending order
        PriorityQueue<Pair<ParticipantDTO, Double>> positive =
            new PriorityQueue<>((a, b) -> Double.compare(b.getValue(), a.getValue()));
        // pq with acsending order
        PriorityQueue<Pair<ParticipantDTO, Double>> negative =
            new PriorityQueue<>(Comparator.comparingDouble(Pair::getValue));

        populatePQs(event, graph, positive, negative);

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
            credit += settlementAmount;
            debt += settlementAmount;

            // Reinsert participants into priority queues if they still have non-zero debt
            if (debt < 0) {
                negative.offer(new Pair<>(debtor, debt));
            }
            if (credit > 0) {
                positive.offer(new Pair<>(creditor, credit));
            }
        }
    }

    private static void populateGraph(EventDTO event,
                                  Graph<ParticipantDTO, DefaultWeightedEdge> graph) {
        // Each participant is a vertex
        for (ParticipantDTO p : event.participants) {
            graph.addVertex(p);
        }

        // Add transactions as edges
        // Each weight is the total amount in each transaction
        // divided by the number of participants
        // (since they're splitting equally)
        for (TransactionDTO t : event.transactions) {
            for (ParticipantDTO p: t.participants) {
                DefaultWeightedEdge e = graph.addEdge(p, t.author);
                graph.setEdgeWeight(e, t.amount.doubleValue() / t.participants.size());
            }
        }
    }

    private static void populatePQs(EventDTO event, Graph<ParticipantDTO,
        DefaultWeightedEdge> graph,
                                    PriorityQueue<Pair<ParticipantDTO, Double>> positive,
                                    PriorityQueue<Pair<ParticipantDTO, Double>> negative) {
        for (ParticipantDTO p : event.participants) {
            double incoming = graph.incomingEdgesOf(p)
                .stream()
                .mapToDouble(graph::getEdgeWeight)
                .sum();
            double outgoing = graph.outgoingEdgesOf(p)
                .stream()
                .mapToDouble(graph::getEdgeWeight)
                .sum();
            if (incoming > outgoing) {
                positive.add(new Pair<>(p, incoming - outgoing));
            } else if (incoming < outgoing) {
                negative.add(new Pair<>(p, incoming - outgoing));
            }
        }
    }

}

