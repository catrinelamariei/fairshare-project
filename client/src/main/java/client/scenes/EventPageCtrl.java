package client.scenes;

import client.MainCtrl;
import client.scenes.javaFXClasses.ParticipantNode;
import client.utils.ServerUtils;
import client.scenes.javaFXClasses.TransactionNode;
import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TransactionDTO;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class EventPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private UUID eventId;
    @FXML
    private VBox transactions;
    @FXML
    private Accordion participants;

    @Inject
    public EventPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.eventId = new UUID(0,1); //temporary placeholder
    }

    public void initialize(URL location, ResourceBundle resources) {
    }

    public void load(UUID id) {
        System.out.println("Initializing EventPage");
        this.eventId = id;
        ParticipantNode.init(); //do some styling

        try {
            EventDTO event = server.getEvent(eventId);

            //load transactions
            for (TransactionDTO ts : event.transactions) {
                transactions.getChildren().add(createTransactionNode(ts));
            }

            //load participants
            for (ParticipantDTO p : event.participants) {
                participants.getPanes().add(new ParticipantNode(p));
            }

        } catch (WebApplicationException e) {
            System.err.printf("Error while fetching EVENT<%s>: %s%n", eventId, e);
        }
    }

    public void gotoHome() {
        mainCtrl.showStartPage();
    }
    public void gotoAdminLogin() {
        mainCtrl.showAdminCheckPage();
    }

    public void copyInviteCode() {
        //get system singleton of clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        //get invite code (String)
        String invCode = "123456789"; //temporary placeholder (content)
        String eventName = "NewYearEvent"; //temporary placeholder (owner)

        //copy data to clipboard
        StringSelection content = new StringSelection(invCode);
        StringSelection owner = new StringSelection(eventName);
        clipboard.setContents(content, owner);

        //display copied for 3 seconds
        System.out.println("Copied to clipboard!"); //temporary placeholder
    }

    public void testAddTransaction() {
        // TEST:
        TransactionDTO tsDTO = new TransactionDTO(
                new UUID(0, 1),
                new Date(),
                "eur",
                new BigDecimal("10.99"),
                new ParticipantDTO("Max", "Well", "mail@me.com", "FR1234"),
                "Burgerzz"
        );
        tsDTO.participants = new HashSet<ParticipantDTO>();
        tsDTO.participants.add(new ParticipantDTO("Bo", "To", "mail", "iban"));

        HBox node = createTransactionNode(tsDTO);
        transactions.getChildren().add(node);
    }

    /**
     * Create a javFX node representing a transaction
     * @param ts transaction to be displayed (data source)
     * @return a node filled with data
     */
    private TransactionNode createTransactionNode(TransactionDTO ts) {
        //date
        Text date = new Text(dateToString(ts.date));

        //main body
        Text desc = new Text(String.format("%s payed %.2f%s for %s",
            ts.author.firstName, ts.amount, ts.currencyCode, ts.subject));
        desc.getStyleClass().add("desc"); //set css class to .desc

        Text particants = new Text("(" +
                ts.participants.stream()
                .map(p -> p.firstName)
                .collect(Collectors.joining(", "))
                + ")"); //concatenate with ", " in between each name
        particants.getStyleClass().add("participantText"); //set css class to .participants

        VBox body = new VBox(desc, particants);

        //image
        Image img = new Image("/client/Images/764599.png", 30d, 30d, true, false); //imageview size
        ImageView imgv = new ImageView(img);
        Button btn = new Button("", imgv);

        //assembling it all
        TransactionNode out = new TransactionNode(); //new TransactionNode (=HBox)
        btn.setOnAction(out::editTransaction); //attach method to button
        out.getChildren().addAll(date, body, btn); //add all nodes to HBox
        out.id = ts.id; //so we can reference it (e.g. for updating)
        out.getStyleClass().add("transaction"); //css class .transaction
        out.setHgrow(body, Priority.ALWAYS); //manage HBox.Hgrow -> make it expand
        Insets insets = new Insets(10.0d);
        out.getChildren().forEach(n -> out.setMargin(n, insets)); //make all children spaced out

        return out;
    }

    public static String dateToString(Date date) {
        Calendar calendar = (new Calendar.Builder()).setInstant(date).build();
        return String.format("%d/%d/%d", calendar.get(Calendar.DATE),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.YEAR));
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
}
