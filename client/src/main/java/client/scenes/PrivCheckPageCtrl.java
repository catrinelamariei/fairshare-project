package client.scenes;

import client.MainCtrl;
import client.utils.ServerUtils;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class PrivCheckPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    public TextField password;
    public Text text;

    @Inject
    public PrivCheckPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        text = new Text();
    }

    public void initialize(URL location, ResourceBundle resources) {}

    public void adminPage() {
        if(password.getText().equals("abc")) {
            mainCtrl.showAdminPage();
        }else{
            text.setStyle("-fx-text-fill: red;");
            text.setText("Wrong code");
        }
    }
}
