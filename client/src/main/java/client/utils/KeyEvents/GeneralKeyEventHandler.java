package client.utils.KeyEvents;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public interface GeneralKeyEventHandler extends EventHandler<KeyEvent> {
    //some universal keyEvents
    @Override
    default void handle(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER -> submit();
        }

        specificHandle(e);
    }

    default void specificHandle(KeyEvent e) {};
    void submit();
}
