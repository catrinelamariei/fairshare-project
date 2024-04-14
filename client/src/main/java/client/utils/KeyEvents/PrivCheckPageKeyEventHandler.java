package client.utils.KeyEvents;

import client.scenes.PrivCheckPageCtrl;
import com.google.inject.Inject;

public class PrivCheckPageKeyEventHandler implements GeneralKeyEventHandler {
    private final PrivCheckPageCtrl privCheckPageCtrl;

    @Inject
    public PrivCheckPageKeyEventHandler(PrivCheckPageCtrl privCheckPageCtrl) {
        this.privCheckPageCtrl = privCheckPageCtrl;
    }

    @Override
    public void submit() {
        privCheckPageCtrl.login();
    }
}
