package client.scenes.javaFXClasses.DataNode;

import client.UserData;
import client.scenes.EventPageCtrl;
import client.utils.ServerUtils;
import commons.DTOs.*;
import javafx.scene.control.TitledPane;

public class DebtNode extends TitledPane {
    //services
    protected final UserData userData;
    protected final ServerUtils server;

    protected final ParticipantDTO debtor;
    public final ParticipantDTO creditor;
    protected final String currencyCode;
    protected final double amount;
    protected final EventDTO event;
    protected final EventPageCtrl ctrl;

    protected DebtNode(ParticipantDTO debtor, ParticipantDTO creditor, String currencyCode,
                       double amount, EventDTO event, ServerUtils server, EventPageCtrl ctrl,
                       UserData userData) {
        this.debtor = debtor;
        this.creditor = creditor;
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.event = event;
        this.server = server;
        this.ctrl = ctrl;
        this.userData = userData;
    }
}
