package client.scenes;

import client.MainCtrl;
import client.utils.ServerUtils;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class EventPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Integer eventId;

    @Inject
    public EventPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.eventId = 1; //temporary placeholder
    }

    public void initialize(URL location, ResourceBundle resources) {}

    public void gotoHome() {
        mainCtrl.showStartPage();
    }
    public void gotoAdminLogin() {
        mainCtrl.showAdminPage();
    }
}
