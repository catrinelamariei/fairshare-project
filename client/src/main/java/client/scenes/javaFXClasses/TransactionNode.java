package client.scenes.javaFXClasses;

import client.scenes.EventPageCtrl;
import client.utils.ServerUtils;
import commons.DTOs.TransactionDTO;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.stream.Collectors;

public class TransactionNode extends HBox {
    private final EventPageCtrl eventPageCtrl;
    public UUID id;
    public TransactionDTO ts;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Create a javFX node representing a transaction
     * @param ts transaction to be displayed (data source)
     */
    public TransactionNode(TransactionDTO ts, EventPageCtrl eventPageCtrl) {
        super(); //initialize HBox part
        this.eventPageCtrl = eventPageCtrl;

        this.ts = ts;

        //date
        Text date = new Text(formatter.format(ts.date));

        //main body
        Text desc = new Text(String.format("%s paid %.2f%s for %s",
            ts.author.firstName.trim(), ts.amount, ts.currencyCode, ts.subject));
        desc.getStyleClass().add("desc"); //set css class to .desc

        Text participants = new Text("(" +
            ts.participants.stream()
                .map(p -> p.firstName.trim())
                .collect(Collectors.joining(", "))
            + ")"); //concatenate with ", " in between each name
        participants.getStyleClass().add("participantText"); //set css class to .participants

        VBox body = new VBox(desc, participants);

        // Delete Button
        Button deleteTransactionButton = new Button("Delete");
        deleteTransactionButton.setOnAction(this::deleteTransaction);

        // Edit Button: image
        Image img = new Image("/client/Images/764599.png", 30d, 30d, true, false); //imageview size
        ImageView imgv = new ImageView(img);
        Button btn = new Button("", imgv);
        btn.setOnAction(this::editTransaction); //attach method to button

        //assembling it all
        this.getChildren().addAll(date, body, btn, deleteTransactionButton); //add all nodes to HBox
        this.id = ts.id; //so we can reference it (e.g. for updating)
        this.getStyleClass().add("transaction"); //css class .transaction
        this.setHgrow(body, Priority.ALWAYS); //manage HBox.Hgrow -> make it expand
        Insets insets = new Insets(10.0d);
        this.getChildren().forEach(n -> this.setMargin(n, insets)); //make all children spaced out
    }

    public void editTransaction (ActionEvent event) {
        // TODO: implement this
        System.out.println("Start editing transaction");

        eventPageCtrl.enableEditing(this);
        eventPageCtrl.fillTransaction((new ServerUtils()).getTransaction(id));
    }

    public void deleteTransaction(ActionEvent event) {
        if (id == null) {
            System.err.println("Error: TransactionNode ID is null.");
            return;
        }

        try {
            ((Pane) this.getParent()).getChildren().remove(this); //remove this node from parent
            (new ServerUtils()).deleteTransaction(id); // TODO: should use singleton
        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing UUID: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
        }
    }
}
