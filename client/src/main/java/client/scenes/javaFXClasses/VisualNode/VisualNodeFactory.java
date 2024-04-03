package client.scenes.javaFXClasses.VisualNode;

import client.MainCtrl;
import client.scenes.EventPageCtrl;
import client.scenes.javaFXClasses.DataNode.DebtNode;
import client.scenes.javaFXClasses.DataNode.EventNode;
import client.scenes.javaFXClasses.DataNode.ParticipantNode;
import client.scenes.javaFXClasses.DataNode.TransactionNode;
import client.scenes.javaFXClasses.NodeFactory;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Provider;
import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TransactionDTO;

public class VisualNodeFactory implements NodeFactory {
    private final MainCtrl mainCtrl;
    private final Provider<EventPageCtrl> epcProvider; //break cyclic dependency
    private final ServerUtils server;

    @Inject
    public VisualNodeFactory(MainCtrl mainCtrl, Provider<EventPageCtrl> epcProvider,
                             ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.epcProvider = epcProvider;
        this.server = server;
    }

    @Override
    public DebtNode createDebtNode(ParticipantDTO debtor, ParticipantDTO creditor,
                                   String currencyCode, double amount) {
        return new VisualDebtNode(debtor, creditor, currencyCode, amount);
    }

    @Override
    public EventNode createEventNode(EventDTO e) {
        return new VisualEventNode(e, mainCtrl);
    }

    @Override
    public ParticipantNode createParticipantNode(ParticipantDTO p) {
        return new VisualParticipantNode(p);
    }

    @Override
    public TransactionNode createTransactionNode(TransactionDTO t) {
        return new VisualTransactionNode(t, epcProvider.get(), server);
    }
}
