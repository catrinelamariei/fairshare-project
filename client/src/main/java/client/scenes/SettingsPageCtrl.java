package client.scenes;

import client.UserData;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class SettingsPageCtrl {
    //Services
    private final ServerUtils server;
    private final UserData userData = UserData.getInstance(); //should be injected

    //Languages

    //Connections
    @FXML
    private TextField urlTextField;
    @FXML
    private Text statusText;

    @Inject
    public SettingsPageCtrl(ServerUtils serverUtils) {
        this.server = serverUtils;
    }

    @FXML
    private void selectLanguage() {}

    @FXML
    private void cancelLanguage() {}

    @FXML
    private void addConnection() {}

    @FXML
    private void testConnection() {
        try {
            var res = server.reach(urlTextField.getText());
            statusText.setText(res.getReasonPhrase());

            if (res.equals(Response.Status.OK)) {
                statusText.setFill(Color.GREEN);
                return;
            }
        } catch (Exception e) {
            System.err.println(e);
            statusText.setText("unavailable");
        }
        statusText.setFill(Color.RED);
    }

    @FXML
    private void selectSavedConnection() {}

}
