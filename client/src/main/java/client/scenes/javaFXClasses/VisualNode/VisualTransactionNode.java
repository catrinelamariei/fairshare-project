package client.scenes.javaFXClasses.VisualNode;

import client.UserData;
import client.scenes.EventPageCtrl;
import client.scenes.javaFXClasses.DataNode.TransactionNode;
import client.utils.*;
import commons.Currency.RateDTO;
import commons.DTOs.*;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class VisualTransactionNode extends TransactionNode {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Create a javFX node representing a transaction
     * @param ts transaction to be displayed (data source)
     */
    protected VisualTransactionNode(TransactionDTO ts, EventPageCtrl eventPageCtrl,
                                    ServerUtils server) {
        super(eventPageCtrl, server, ts.id);

        //date
        Text date = new Text(formatter.format(ts.date));

        String currencySymbol = switch (ts.currencyCode) {
            case "EUR" -> "\u20AC";
            case "USD" -> "\u0024";
            case "GBP" -> "\u00A3";
            default -> ts.currencyCode;
        };
            
        RateDTO rate = RateUtils.getRate(ts.currencyCode,
                UserData.getInstance().getCurrencyCode(), ts.date);
        BigDecimal amountInPreferred = ts.amount.multiply(BigDecimal.valueOf(rate.rate));

        Text desc = new Text(String.format("%s paid %.2f%s for %s",
            ts.author.firstName.trim(), amountInPreferred,
                UserData.getInstance().getCurrencyCode(), ts.subject));
                
        desc.getStyleClass().add("desc"); //set css class to .desc

        Text participants = new Text("(" +
            ts.participants.stream()
                .map(p -> p.firstName.trim())
                .collect(Collectors.joining(", "))
            + ")"); //concatenate with ", " in between each name
        participants.getStyleClass().add("participantText");

        participants.setWrappingWidth(400.0);
        desc.setWrappingWidth(400.0);
        //set css class to .participants

        TagDTO tag = ts.tags.stream().findFirst().orElse(null);
        Label tagTxt;
        VBox body;
        if (tag != null) {
            tagTxt = new Label(tag.name);
            tagTxt.getStyleClass().add("tagText");
            tagTxt.setStyle("-fx-background-color: " + tag.color.colorCode + ";");
            tagTxt.setWrapText(true);
            body = new VBox(desc, participants, tagTxt);
        } else {
            body = new VBox(desc, participants);
        }

        // Delete Button
        Button deleteTransactionButton = new Button("Delete");
        deleteTransactionButton.setStyle("-fx-text-fill: #ff0000;");
        deleteTransactionButton.setOnAction(this::deleteTransaction);
        deleteTransactionButton.setFont(Font.font("System", FontWeight.BOLD, 20.0));

        // Edit Button: some options below
//        Image img = new Image("/client/Images/edit-button-3.png", 25d, 25d, true, false);
//        Image img = new Image("/client/Images/edit-button-1.png", 30d, 30d, true, false);
        Image img = new Image("/client/Images/edit-button-2.png", 20d, 20d, true, false);


        ImageView imgv = new ImageView(img);
        Button btn = new Button("Edit", imgv);
        btn.setFont(Font.font("System", FontWeight.BOLD, 20.0));
        btn.setOnAction(this::editTransaction); //attach method to button

        //assembling it all
        this.getChildren().addAll(date, body, btn, deleteTransactionButton); //add all nodes to HBox
        this.getStyleClass().add("transaction"); //css class .transaction
        this.setHgrow(body, Priority.ALWAYS); //manage HBox.Hgrow -> make it expand
        Insets insets = new Insets(10.0d);
        this.getChildren().forEach(n -> this.setMargin(n, insets)); //make all children spaced out
    }

    public void editTransaction (ActionEvent event) {
        // TODO: implement this
        System.out.println("Start editing transaction");

        eventPageCtrl.enableEditing(this);
        eventPageCtrl.fillTransaction(server.getTransaction(id));
    }

    public void deleteTransaction(ActionEvent event) {
        if (id == null) {
            System.err.println("Error: TransactionNode ID is null.");
            return;
        }

        try {
            ((Pane) this.getParent()).getChildren().remove(this); //remove this node from parent
            TransactionDTO old = server.getTransaction(id);
            eventPageCtrl.undoService.addAction(UndoService.TsAction.DELETE, old);
            server.deleteTransaction(id);


        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing UUID: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
        }
    }
}
