package client.utils;

import client.scenes.EventPageCtrl;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import commons.DTOs.TransactionDTO;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

public class UndoService {
    // to access and edit old transactions (like UUID) from outside the runnable scopes
    public List<TransactionDTO> oldTSList = new ArrayList<>();
    private final Stack<Runnable> undoActions = new Stack<>();

    // to actually do actions
    private final EventPageCtrl eventPageCtrl;
    private final ServerUtils server;

    @Inject
    public UndoService(@Assisted EventPageCtrl eventPageCtrl, @Assisted ServerUtils server) {
        this.eventPageCtrl = eventPageCtrl;
        this.server = server;
    }

    /**
     * adds an action that can be undone later
     * @param action type of action executed
     * @param oldTS old transaction (in case of CREATE the new transaction)
     */
    public void addAction(tsAction action, TransactionDTO oldTS) {
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
        oldTSList.remove(t);

    }
    private void undoUpdate(TransactionDTO t) {
        oldTSList.remove(t);

    }
    private void undoDelete(TransactionDTO t) {
        oldTSList.remove(t);

    }

    /**
     * class to easily choose what actions was performed
     */
    public static enum tsAction{
        CREATE,
        UPDATE,
        DELETE;
    }
}
