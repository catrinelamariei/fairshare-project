package client.scenes.javaFXClasses;

import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TransactionDTO;
import javafx.util.Pair;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import java.util.*;


public class DebtGraph extends DirectedWeightedPseudograph<ParticipantDTO, DefaultWeightedEdge> {
    public PriorityQueue positive;
    public PriorityQueue negative;

    public DebtGraph(EventDTO event) {
        super(DefaultWeightedEdge.class);

        populateGraph(event);

        populatePQs();
    }

    private void populateGraph(EventDTO event) {
        // Each participant is a vertex
        event.participants.forEach(this::addVertex);

        // Add transactions as edges
        // Each weight is the total amount in each transaction
        // divided by the number of participants
        // (since they're splitting equally)
        for (TransactionDTO t : event.transactions) {
            // assign remainder to a random unlucky participant :)
            double equalSplitAmount = t.amount.doubleValue() / t.participants.size();
            double remainder = t.amount.doubleValue() % t.participants.size();
            List<ParticipantDTO> tempList = new ArrayList<>(t.participants);
            ParticipantDTO unluckyParticipant =
                tempList.get(new Random().nextInt(t.participants.size()));

            for (ParticipantDTO p : t.participants) {
                DefaultWeightedEdge e = this.addEdge(p, t.author);
                if (p.equals(unluckyParticipant)) {
                    this.setEdgeWeight(e, equalSplitAmount + remainder);
                } else {
                    this.setEdgeWeight(e, equalSplitAmount);
                }
            }
        }
    }


    // get total amount each participant owes/is owed
    // put them in two priority queues,
    // 1 for if they owe money,
    // 1 for if they are owed money
    private void populatePQs() {
        Comparator<Pair<ParticipantDTO, Double>> cmp = Comparator.comparingDouble(Pair::getValue);
        // pq with descending order
        positive = new PriorityQueue<>(cmp.reversed());
        // pq with acsending order
        negative = new PriorityQueue<>(cmp);
        for (ParticipantDTO p : this.vertexSet()) {
            double incoming = this.incomingEdgesOf(p)
                .stream()
                .mapToDouble(this::getEdgeWeight)
                .sum();
            double outgoing = this.outgoingEdgesOf(p)
                .stream()
                .mapToDouble(this::getEdgeWeight)
                .sum();
            if (incoming > outgoing) {
                positive.add(new Pair<>(p, incoming - outgoing));
            } else if (incoming < outgoing) {
                negative.add(new Pair<>(p, incoming - outgoing));
            }
        }
    }
}
