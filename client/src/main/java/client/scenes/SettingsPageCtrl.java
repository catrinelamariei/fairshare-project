package client.scenes;

import client.Main;
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
import java.util.List;
import java.util.ResourceBundle;

public class SettingsPageCtrl implements Initializable {
    //Services
    private final ServerUtils server;
    private final UserData userData = UserData.getInstance(); //should be injected

    //Languages
    @FXML
    private ChoiceBox<String> languageChoiceBox;

    //Currencies
    @FXML
    private ChoiceBox<String> currencyChoiceBox;

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
        //load all languages
        languageChoiceBox.setItems(FXCollections.observableList(
                StartPageCtrl.getAllLanguageCodes()));
        languageChoiceBox.setValue(userData.getLanguageCode());

        //load all currencies
        currencyChoiceBox.setItems(FXCollections.observableList(List.of("EUR", "USD", "CHF")));
        currencyChoiceBox.setValue(userData.getCurrencyCode());

        //load all urls
        urlList.setCellFactory(list -> new UrlListCell());
        urlList.setItems(FXCollections.observableList(userData.getUrlList()));
        urlList.setValue(userData.getServerURL());
    }

    @FXML
    private void selectLanguage() {
        userData.setLanguageCode(languageChoiceBox.getValue());
        Main.initializeUI(languageChoiceBox.getValue());
    }

    @FXML
    private void cancelLanguage() {
        languageChoiceBox.setValue(userData.getLanguageCode());
    }

    @FXML
    private void selectCurrency() {
        userData.setCurrencyCode(currencyChoiceBox.getValue());
    }

    @FXML
    private void cancelCurrency() {
        currencyChoiceBox.setValue(userData.getCurrencyCode());
    }

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
