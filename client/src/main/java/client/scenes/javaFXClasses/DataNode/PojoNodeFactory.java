package client.scenes.javaFXClasses.DataNode;

import client.MainCtrl;
import client.UserData;
import client.scenes.EventPageCtrl;
import client.scenes.javaFXClasses.NodeFactory;
import client.utils.ServerUtils;
import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TransactionDTO;

public class PojoNodeFactory implements NodeFactory {
    private final MainCtrl mainCtrl;
    private final EventPageCtrl eventPageCtrl;
    private final ServerUtils server;

    public PojoNodeFactory(MainCtrl mainCtrl, EventPageCtrl eventPageCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.eventPageCtrl = eventPageCtrl;
        this.server = server;
    }

    @Override
    public DebtNode createDebtNode(ParticipantDTO debtor, ParticipantDTO creditor,
                                   String currencyCode, double amount) {
        return new DebtNode();
    }

    @Override
    public EventNode createEventNode(EventDTO e) {
        return new EventNode(mainCtrl, new UserData.Pair<>(e.id, e.name));
    }

    @Override
    public ParticipantNode createParticipantNode(ParticipantDTO p) {
        return new ParticipantNode(p.id, p.getFullName(), eventPageCtrl);
    }

    @Override
    public TransactionNode createTransactionNode(TransactionDTO t) {
        return new TransactionNode(eventPageCtrl, server, t.id);
    }
}
