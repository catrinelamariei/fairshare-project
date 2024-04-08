package client.scenes.javaFXClasses.DataNode;

import client.scenes.EventPageCtrl;
import client.utils.ServerUtils;
import commons.DTOs.*;
import javafx.scene.control.TitledPane;

public class DebtNode extends TitledPane {

    protected final ParticipantDTO debtor;
    public final ParticipantDTO creditor;
    protected final String currencyCode;
    protected final double amount;
    protected final EventDTO event;
    protected final ServerUtils server;
    protected final EventPageCtrl ctrl;

    protected DebtNode(ParticipantDTO debtor, ParticipantDTO creditor,
                       String currencyCode, double amount,
                       EventDTO event, ServerUtils server,
                       EventPageCtrl ctrl) {
        this.debtor = debtor;
        this.creditor = creditor;
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.event = event;
        this.server = server;
        this.ctrl = ctrl;
    }
}
