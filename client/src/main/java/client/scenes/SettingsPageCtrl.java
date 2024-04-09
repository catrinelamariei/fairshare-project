package client.scenes;

import client.UserData;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsPageCtrl implements Initializable {
    //Services
    private final ServerUtils server;
    private final UserData userData = UserData.getInstance(); //should be injected

    //Languages

    //Connections
    @FXML
    private TextField urlTextField;
    @FXML
    private Text statusText;
    @FXML
    private ComboBox<String> urlList;

    @Inject
    public SettingsPageCtrl(ServerUtils serverUtils) {
        this.server = serverUtils;
    }

    @Override
    public void initialize(URL url1, ResourceBundle resourceBundle) {
        //load all urls
        urlList.setCellFactory(list -> new UrlListCell());
        urlList.setItems(FXCollections.observableList(userData.getUrlList()));
        urlList.setValue(userData.getServerURL());
    }

    @FXML
    private void selectLanguage() {}

    @FXML
    private void cancelLanguage() {}

    @FXML
    private void addConnection() {
        userData.setSelectedURL(urlTextField.getText());
        //update saved URLS
        urlList.setItems(FXCollections.observableList(userData.getUrlList()));
        urlList.setValue(urlTextField.getText());
        urlTextField.clear();
    }

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
    private void selectSavedConnection() {
        userData.setSelectedURL(urlList.getValue());
    }

    //utility methods
    private class UrlListCell extends ListCell<String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) return;

            setText(item);
            try {
                if (server.reach(item).equals(Response.Status.OK)) {
                    setTextFill(Color.GREEN);
                    return;
                }
            } catch (ProcessingException e) {}
            setTextFill(Color.RED);
        }
    }
}
