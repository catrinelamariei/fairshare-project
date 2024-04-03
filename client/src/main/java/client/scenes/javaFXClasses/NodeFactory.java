package client.scenes.javaFXClasses;

import client.scenes.javaFXClasses.DataNode.DebtNode;
import client.scenes.javaFXClasses.DataNode.EventNode;
import client.scenes.javaFXClasses.DataNode.ParticipantNode;
import client.scenes.javaFXClasses.DataNode.TransactionNode;
import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TransactionDTO;

public interface NodeFactory {
    public DebtNode createDebtNode(ParticipantDTO debtor, ParticipantDTO creditor,
                                   String currencyCode, double amount);
    public EventNode createEventNode(EventDTO e);
    public ParticipantNode createParticipantNode(ParticipantDTO p);
    public TransactionNode createTransactionNode(TransactionDTO t);
}
