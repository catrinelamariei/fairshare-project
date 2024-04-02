package client.utils;

import client.scenes.EventPageCtrl;
import com.google.inject.Inject;
import javafx.scene.input.KeyEvent;

public class EventPageKeyEventHandler implements GeneralKeyEventHandler{
    private final UndoService undoService;
    //dependant on current state of EventPage
    public Runnable submitAction = () -> System.err.println("No action set");

    public EventPageKeyEventHandler(UndoService undoService) {
        this.undoService = undoService;
    }

    @Override
    public void specificHandle(KeyEvent e) {
        if (e.isControlDown()) {
            switch (e.getCode()) {
                case Z -> undoService.undo();
            }
        }
    }

    @Override
    public void submit() {
        submitAction.run();
    }
}
