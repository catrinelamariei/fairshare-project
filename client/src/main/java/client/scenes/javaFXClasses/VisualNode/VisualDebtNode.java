package client.scenes.javaFXClasses.VisualNode;

import client.Main;
import client.UserData;
import client.scenes.EventPageCtrl;
import client.scenes.javaFXClasses.DataNode.DebtNode;
import client.utils.*;
import commons.Currency.RateDTO;
import commons.DTOs.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.util.*;

public class VisualDebtNode extends DebtNode {


    protected VisualDebtNode(ParticipantDTO debtor, ParticipantDTO creditor, String currencyCode,
                             double amount, EventDTO event, ServerUtils server, EventPageCtrl ctrl,
                             UserData userData) {
        super(debtor, creditor, currencyCode, amount, event, server, ctrl, userData);

        String txt = null;
        if (userData.getCurrencyCode().equals(currencyCode)) {
            txt = String.format("%s gives %.2f%s to %s",
                    debtor.getFullName(), amount,
                    currencyCode, creditor.getFullName());
        } else {
            RateDTO rate = RateUtils.getRate(currencyCode, userData.getCurrencyCode(), new Date(),
                    userData);
            BigDecimal amountInPreferred = BigDecimal.valueOf(amount)
                    .multiply(BigDecimal.valueOf(rate.rate));
            txt = String.format("%s gives %.2f%s(%.2f%s) to %s",
                    debtor.getFullName(), amount,
                    "EUR",amountInPreferred,
                    userData.getCurrencyCode(), creditor.getFullName());
        }

        this.setText(txt);


        Button receivedButton = new Button(Main.getTranslation("mark_as_received"));

        // a button that creates a transaction for the debt repayment
        // if the event already has a tag "debt", it will be used
        // otherwise a new tag will be created
        receivedButton.setOnAction(e -> {
            ctrl.debts.getPanes().remove(this);
            debtToTransaction(debtor, creditor, currencyCode,
                    amount, event, server, ctrl);
        });


        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox container;
        // display bank info if available
        Text email = new Text("Email: " + creditor.email);
        VBox vbox;
        if (!creditor.iban.equals("-") && !creditor.bic.equals("-")) {
            Text iban = new Text("IBAN:" + creditor.iban);
            Text info = new Text(Main.getTranslation("bank_info"));
            Text bic = new Text("BIC: " + creditor.bic);
            vbox = new VBox(info, email, iban, bic);
        } else if (!creditor.iban.equals("-") && creditor.bic.equals("-")) {
            Text info = new Text(Main.getTranslation("iban_available"));
            vbox = new VBox(info, email, new Text("IBAN: " + creditor.iban));
        } else if (creditor.iban.equals("-") && !creditor.bic.equals("-")) {
            Text info = new Text(Main.getTranslation("bic_available"));
            vbox = new VBox(info, email, new Text("BIC: " + creditor.bic));
        } else {
            Text info = new Text(Main.getTranslation("no_bank_info"));
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
        TagDTO debtTag = event.tags
                .stream()
                .filter(tag -> tag.name.equals("debt"))
                .findFirst()
                .get();
        TransactionDTO ts = new TransactionDTO(null, event.id, new Date(), currencyCode,
                BigDecimal.valueOf(amount), debtor, new HashSet<>(Arrays.asList(creditor)),
                new HashSet<>(Arrays.asList(debtTag)), Main.getTranslation("debt_repayment"));
        ctrl.createTransaction(ts);
    }

}
