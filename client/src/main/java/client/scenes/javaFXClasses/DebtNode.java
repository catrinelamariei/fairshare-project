package client.scenes.javaFXClasses;

import commons.DTOs.ParticipantDTO;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class DebtNode extends HBox {

    public DebtNode(ParticipantDTO debtor, ParticipantDTO creditor,
                    String currencyCode, double amount) {
        super(); //initialize HBox part

        Text txt = new Text(String.format("%s gives %.2f%s to %s",
            debtor.firstName+" "+debtor.lastName, amount,
            currencyCode, creditor.firstName+" "+creditor.lastName));

        Button receivedButton = new Button("Mark as received");

        this.getChildren().addAll(txt, receivedButton);
        Insets insets = new Insets(10.0d);
        this.getChildren().forEach(n -> this.setMargin(n, insets));
    }

}
