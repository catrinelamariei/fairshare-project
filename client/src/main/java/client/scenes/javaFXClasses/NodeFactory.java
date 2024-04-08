package client.scenes.javaFXClasses;

import client.scenes.EventPageCtrl;
import client.scenes.javaFXClasses.DataNode.*;
import client.utils.ServerUtils;
import commons.DTOs.*;

public interface NodeFactory {
    public DebtNode createDebtNode(ParticipantDTO debtor, ParticipantDTO creditor,
                                   String currencyCode, double amount, EventDTO event,
                                   ServerUtils server, EventPageCtrl ctrl);
    public EventNode createEventNode(EventDTO e);
    public ParticipantNode createParticipantNode(ParticipantDTO p);
    public TransactionNode createTransactionNode(TransactionDTO t);
}
