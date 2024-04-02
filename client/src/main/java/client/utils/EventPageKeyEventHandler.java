package client.utils;

import client.scenes.EventPageCtrl;
import com.google.inject.Inject;
import javafx.scene.input.KeyEvent;

public class EventPageKeyEventHandler implements GeneralKeyEventHandler{
    private EventPageCtrl eventPageCtrl;
    public Runnable submitAction = () -> System.err.println("No action set");

    @Inject
    public EventPageKeyEventHandler(EventPageCtrl eventPageCtrl) {
        this.eventPageCtrl = eventPageCtrl;
    }

    @Override
    public void specificHandle(KeyEvent e) {
        if (e.isControlDown()) {
            switch (e.getCode()) {
                case Z -> eventPageCtrl.undo();
            }
        }
    }

    @Override
    public void submit() {
        submitAction.run();
    }
}
