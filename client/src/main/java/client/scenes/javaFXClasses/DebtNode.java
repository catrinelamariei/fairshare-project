package client.scenes.javaFXClasses;

import commons.DTOs.ParticipantDTO;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DebtNode extends TitledPane {

    public DebtNode(ParticipantDTO debtor, ParticipantDTO creditor,
                    String currencyCode, double amount) {

        super(String.format("%s gives %.2f%s to %s",
            debtor.getFullName(), amount,
            currencyCode, creditor.getFullName()), null);



        boolean availability = !creditor.iban.equals("-") || !creditor.bic.equals("-");

        Button receivedButton = new Button("Mark as received");
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox container;
        if (availability) {
            Text info = new Text("Bank information is available");
            Text iban = new Text("IBAN:" + creditor.iban);
            Text bic = new Text("BIC: " + creditor.bic);
            VBox vbox = new VBox(info, iban, bic);
            container = new HBox(vbox, spacer, receivedButton);
        } else {
            Text info = new Text("Bank information is unavailable");
            container = new HBox(info, spacer, receivedButton);

        }
        Insets insets = new Insets(10.0d);
        container.getChildren().forEach(n -> container.setMargin(n, insets));
        this.setContent(container);
    }

}
