package client.scenes.javaFXClasses.VisualNode;

import client.MainCtrl;
import client.scenes.EventPageCtrl;
import client.scenes.javaFXClasses.DataNode.*;
import client.scenes.javaFXClasses.NodeFactory;
import client.utils.EventJson.EventJsonUtil;
import client.utils.ServerUtils;
import com.google.inject.*;
import commons.DTOs.*;

public final class VisualNodeFactory implements NodeFactory {
    private final MainCtrl mainCtrl;
    private final Provider<EventPageCtrl> epcProvider; //break cyclic dependency
    private final ServerUtils server;
    private final EventJsonUtil jsonUtil;

    @Inject
    public VisualNodeFactory(MainCtrl mainCtrl, Provider<EventPageCtrl> epcProvider,
                             ServerUtils server, EventJsonUtil jsonUtil) {
        this.mainCtrl = mainCtrl;
        this.epcProvider = epcProvider;
        this.server = server;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public DebtNode createDebtNode(ParticipantDTO debtor, ParticipantDTO creditor,
                                   String currencyCode, double amount,
                                   EventDTO event, ServerUtils server, EventPageCtrl ctrl) {
        return new VisualDebtNode(debtor, creditor, currencyCode, amount,
                event, server, ctrl);
    }

    @Override
    public EventNode createEventNode(EventDTO e) {
        return new VisualEventNode(e, mainCtrl, jsonUtil);
    }

    @Override
    public ParticipantNode createParticipantNode(ParticipantDTO p) {
        return new VisualParticipantNode(p, epcProvider.get());
    }

    @Override
    public TransactionNode createTransactionNode(TransactionDTO t) {
        return new VisualTransactionNode(t, epcProvider.get(), server);
    }
}
