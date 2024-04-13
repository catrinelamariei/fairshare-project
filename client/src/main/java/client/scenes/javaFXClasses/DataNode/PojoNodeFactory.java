package client.scenes.javaFXClasses.DataNode;

import client.*;
import client.scenes.EventPageCtrl;
import client.scenes.javaFXClasses.NodeFactory;
import client.utils.EventJsonUtil;
import client.utils.ServerUtils;
import commons.DTOs.*;

public class PojoNodeFactory implements NodeFactory {
    private final MainCtrl mainCtrl;
    private final EventPageCtrl eventPageCtrl;
    private final ServerUtils server;
    private final EventJsonUtil jsonUtil;
    private final UserData userData;

    public PojoNodeFactory(MainCtrl mainCtrl, EventPageCtrl eventPageCtrl, ServerUtils server,
                           EventJsonUtil jsonUtil, UserData userData) {
        this.mainCtrl = mainCtrl;
        this.eventPageCtrl = eventPageCtrl;
        this.server = server;
        this.jsonUtil = jsonUtil;
        this.userData = userData;
    }

    @Override
    public DebtNode createDebtNode(ParticipantDTO debtor, ParticipantDTO creditor,
                                   String currencyCode, double amount, EventDTO event,
                                   ServerUtils server, EventPageCtrl ctrl) {
        return new DebtNode(debtor, creditor, currencyCode, amount, event, server, ctrl, userData);
    }

    @Override
    public EventNode createEventNode(EventDTO e) {
        return new EventNode(mainCtrl, jsonUtil, userData, new UserData.Pair<>(e.id, e.name),
                server);
    }

    @Override
    public ParticipantNode createParticipantNode(ParticipantDTO p) {
        return new ParticipantNode(p.id, p.getFullName(), eventPageCtrl, userData, server);
    }

    @Override
    public TransactionNode createTransactionNode(TransactionDTO t) {
        return new TransactionNode(eventPageCtrl, server, t.id, userData);
    }
}
