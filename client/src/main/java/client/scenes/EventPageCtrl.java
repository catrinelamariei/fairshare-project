package client.scenes;

import client.MainCtrl;
import client.utils.ServerUtils;
import commons.DTOs.TransactionDTO;
import commons.Participant;
import commons.Transaction;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
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
import java.net.URL;
import java.util.ResourceBundle;

public class EventPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Integer eventId;

    @Inject
    public EventPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.eventId = 1; //temporary placeholder
    }

    public void initialize(URL location, ResourceBundle resources) {
        // TODO: fetch and display transactions + participants
        // TODO: set parent container of transactions to .transaction class
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

    /**
     * Create a javFX node representing a transaction
     * @param ts transaction to be displayed (data source)
     * @return a node filled with data
     */
    private HBox createTransactionNode(TransactionDTO ts) {
        //date
        Text date = new Text(ts.date.toString());

        //main body
        Text desc = new Text(String.format("%s payed %.2f%s for %s"),
            ts.author, ts.amount, ts.currencyCode, ts.subject);
        desc.getStyleClass().add("desc"); //set css class to .desc

        Text particants = new Text(ts.participants.toString());
        particants.getStyleClass().add("participants"); //set css class to .participants

        VBox body = new VBox(desc, particants);

        //image
        Image img = new Image("@../Images/764599.png", 30d, 30d, true, false); //set imageview size
        ImageView imgv = new ImageView(img);
        Button btn = new Button("", imgv);

        HBox out = new HBox(date, body, btn);
        out.setHgrow(body, Priority.ALWAYS); //manage hbox.hgrow -> make it expand

        return out;
    }

    /**
     * Create a javFX node representing a participant
     * @param p participant to be displayed (data source)
     * @return a node filled with data
     */
    private TitledPane createParticipantNode(Participant p) {
        return null;
    }
}
