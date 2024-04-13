package client.utils.KeyEvents;

import client.scenes.PrivCheckPageCtrl;
import com.google.inject.Inject;

public class PrivCheckPageKeyEventHandler implements GeneralKeyEventHandler {
    private PrivCheckPageCtrl privCheckPageCtrl;

    @Inject
    private PrivCheckPageKeyEventHandler(PrivCheckPageCtrl privCheckPageCtrl) {
        this.privCheckPageCtrl = privCheckPageCtrl;
    }

    @Override
    public void submit() {
        privCheckPageCtrl.login();
    }
}
