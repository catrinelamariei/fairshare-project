package client.scenes;

import client.MainCtrl;
import client.utils.ServerUtils;
import javafx.fxml.Initializable;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class TransactionPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public TransactionPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing Transaction Page");
    }

    public void gotoHome() {
        mainCtrl.showStartPage();
    }
    public void gotoAdminLogin() {
        mainCtrl.showAdminCheckPage();
    }

}
