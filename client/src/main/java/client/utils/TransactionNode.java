package client.utils;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import java.util.UUID;

public class TransactionNode extends HBox {
    public UUID id;

    public TransactionNode(Node... nodes) {
        super(nodes);
    }

    public void editTransaction (ActionEvent event) {
        // TODO: implement this
        System.out.println("Start editing transaction");
    }
}
