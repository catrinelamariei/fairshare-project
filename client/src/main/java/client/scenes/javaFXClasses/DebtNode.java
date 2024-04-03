package client.scenes.javaFXClasses;

import client.scenes.EventPageCtrl;
import client.utils.ServerUtils;
import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TagDTO;
import commons.DTOs.TransactionDTO;
import commons.Tag;
import jakarta.ws.rs.WebApplicationException;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

public class DebtNode extends TitledPane {

    public ParticipantDTO creditor;

    public DebtNode(ParticipantDTO debtor, ParticipantDTO creditor,
                    String currencyCode, double amount,
                    EventDTO event, ServerUtils server, EventPageCtrl ctrl) {

        super(String.format("%s gives %.2f%s to %s",
                debtor.getFullName(), amount,
                currencyCode, creditor.getFullName()), null);

        this.creditor = creditor;

        Button receivedButton = new Button("Mark as received");

        // a button that creates a transaction for the debt repayment
        // if the event already has a tag "debt", it will be used
        // otherwise a new tag will be created
        receivedButton.setOnAction(e -> {
            ctrl.debts.getPanes().remove(this);
            debtToTransaction(debtor, creditor, currencyCode, amount, event, server, ctrl);
        });


        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox container;
        // display bank info if available
        Text email = new Text("Email: " + creditor.email);
        VBox vbox;
        if (!creditor.iban.equals("-") && !creditor.bic.equals("-")) {
            Text iban = new Text("IBAN:" + creditor.iban);
            Text info = new Text("Bank information is available");
            Text bic = new Text("BIC: " + creditor.bic);
            vbox = new VBox(info, email, iban, bic);
        } else if (!creditor.iban.equals("-") && creditor.bic.equals("-")) {
            Text info = new Text("IBAN is available");
            vbox = new VBox(info, email, new Text("IBAN: " + creditor.iban));
        } else if (creditor.iban.equals("-") && !creditor.bic.equals("-")) {
            Text info = new Text("BIC is available");
            vbox = new VBox(info, email, new Text("BIC: " + creditor.bic));
        } else {
            Text info = new Text("Bank information is unavailable");
            vbox = new VBox(info, email);
        }
        container = new HBox(vbox, spacer, receivedButton);
        Insets insets = new Insets(10.0d);
        container.getChildren().forEach(n -> container.setMargin(n, insets));
        this.setContent(container);
    }

    private static void debtToTransaction(ParticipantDTO debtor, ParticipantDTO creditor,
                                          String currencyCode, double amount, EventDTO event,
                                          ServerUtils server, EventPageCtrl ctrl) {
        TagDTO debtTag;
        Optional<TagDTO> optionalDebtTag = event.tags
                .stream()
                .filter(tag -> tag.name.equals("debt"))
                .findFirst();
        if (optionalDebtTag.isEmpty()) {
            debtTag = new TagDTO(null, event.id, "debt", Tag.Color.ORANGE);
            try {
                server.postTag(debtTag);
            } catch (WebApplicationException err) {
                System.err.println("Error creating tag for debt: " + err.getMessage());
                return;
            }
        } else {
            debtTag = optionalDebtTag.get();
        }
        TransactionDTO ts = new TransactionDTO(null, event.id, new Date(), currencyCode,
                BigDecimal.valueOf(amount), debtor, new HashSet<>(Arrays.asList(creditor)),
                new HashSet<>(Arrays.asList(debtTag)), "Debt repayment");
        try {
            ts = server.postTransaction(ts);
            ctrl.transactions.getChildren().add(new TransactionNode(ts));
        } catch (WebApplicationException err) {
            System.err.println("Error creating transaction from debt: " + err.getMessage());
            err.printStackTrace();
        }
    }

}
