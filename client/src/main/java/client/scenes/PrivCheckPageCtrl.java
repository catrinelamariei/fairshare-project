package client.scenes;

import client.MainCtrl;
import client.utils.ServerUtils;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class PrivCheckPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public PrivCheckPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize(URL location, ResourceBundle resources) {}

    public void adminPage() {
        mainCtrl.showAdminPage();
    }
}
