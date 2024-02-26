package client.scenes;

import client.MainCtrl;
import client.utils.ServerUtils;
import javafx.fxml.Initializable;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class StartPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public StartPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize(URL location, ResourceBundle resources) {}

    public void eventPage() {
        mainCtrl.showEventPage();
    }
    public void adminPage() {
        mainCtrl.showAdminCheckPage();
    }

}
