package client.utils;

import client.scenes.EventPageCtrl;
import client.scenes.javaFXClasses.TransactionNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import commons.DTOs.TransactionDTO;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class UndoService {
    // to access and edit old transactions (like UUID) from outside the runnable scopes
    // we never get from this, we only update ids in this
    // we never remove because we might remove references used multiple times,
    // and I am not about to start managing references/pointers in java
    public List<TransactionDTO> oldTSList = new ArrayList<>();
    private final Stack<Runnable> undoActions = new Stack<>();

    // to actually do actions
    private final EventPageCtrl eventPageCtrl;
    private final ServerUtils server;

    @Inject
    public UndoService(@Assisted EventPageCtrl eventPageCtrl, @Assisted ServerUtils server,
                       @Autowired VBox transactions) {
        this.eventPageCtrl = eventPageCtrl;
        this.server = server;
    }

    /**
     * adds an action that can be undone later
     * @param action type of action executed
     * @param oldTS old transaction (in case of CREATE the new transaction)
     */
    public void addAction(TsAction action, TransactionDTO oldTS) {
        if (oldTS == null) throw new IllegalArgumentException();

        oldTSList.add(oldTS);
        Runnable newAction = switch (action) {
            case CREATE -> () -> undoCreate(oldTS);
            case UPDATE -> (() -> undoUpdate(oldTS));
            case DELETE -> (() -> undoDelete(oldTS));
        };
        undoActions.push(newAction);
    }

    /**
     * undoes the last added action if present
     * otherwise prints error msg
     */
    public void undo() {
        try {
            undoActions.pop().run();
        } catch (EmptyStackException e) {
            System.err.println("nothing to undo");
        }
    }

    /**
     * resets all actions
     */
    public void clear() {
        this.oldTSList.clear();
        this.undoActions.clear();
    }

    //private specific handlers

    private void undoCreate(TransactionDTO t) {
        server.deleteTransaction(t.id);
        eventPageCtrl.transactions.getChildren().removeAll(
            eventPageCtrl.transactions.getChildren().stream()
                .filter(node -> ((TransactionNode) node).id.equals(t.id)).toList());
    }

    private void undoUpdate(TransactionDTO t) {
        ObservableList<Node> children = eventPageCtrl.transactions.getChildren();

        t = server.putTransaction(t);
        UUID id = t.id;

        int index = children.indexOf(children.stream().map(TransactionNode.class::cast)
                        .filter(node -> node.id.equals(id)).findAny().get());
        children.set(index, new TransactionNode(t, eventPageCtrl, server));
    }

    /**
     * when we recreate a transaction the id changes,
     * causing previous update undo actions to become invalid
     */
    private void undoDelete(TransactionDTO t) {
        ObservableList<Node> children = eventPageCtrl.transactions.getChildren();

        UUID oldID = t.id;
        t = server.postTransaction(t);
        UUID newID = t.id; //because otherwise lambda unhappy
        oldTSList.stream()
            .filter(oldTS -> oldTS.id.equals(oldID))
            .forEach(oldTS -> oldTS.id = newID);


        children.add(new TransactionNode(t, eventPageCtrl, server));
    }

    /**
     * class to easily choose what actions was performed
     */
    public static enum TsAction {
        CREATE,
        UPDATE,
        DELETE;
    }
}
