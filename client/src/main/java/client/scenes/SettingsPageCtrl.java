package client.scenes;

import client.*;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

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
        languageChoiceBox.valueProperty().addListener((obs, oldVal, newVal) ->
                valueChanged(languageChoiceBox, userData.getLanguageCode(), newVal));

        //load all currencies
        currencyChoiceBox.setItems(FXCollections.observableList(List.of("EUR", "USD", "CHF")));
        currencyChoiceBox.setValue(userData.getCurrencyCode());
        currencyChoiceBox.valueProperty().addListener((obs, oldVal, newVal) ->
                valueChanged(currencyChoiceBox, userData.getCurrencyCode(), newVal));

        //load all urls
        urlList.setCellFactory(list -> new UrlListCell());
        urlList.setItems(FXCollections.observableArrayList(userData.getUrlList()));
        urlList.setValue(userData.getServerURL());
        urlList.valueProperty().addListener((obs, oldVal, newVal) ->
                valueChanged(urlList, userData.getServerURL(), newVal));

        urlTextField.textProperty().addListener((obs, oldVal, newVal) -> urlTextChanged());

        selectedURLAvailabiltyStyle();
    }

    @FXML
    private void selectLanguage() {
        userData.setLanguageCode(languageChoiceBox.getValue());
        Main.initializeUI(languageChoiceBox.getValue());
        languageChoiceBox.getStyleClass().removeAll("ChangedValue");
    }

    @FXML
    private void cancelLanguage() {
        languageChoiceBox.setValue(userData.getLanguageCode());
        languageChoiceBox.getStyleClass().removeAll("ChangedValue");
    }

    @FXML
    private void selectCurrency() {
        userData.setCurrencyCode(currencyChoiceBox.getValue());
        currencyChoiceBox.getStyleClass().removeAll("ChangedValue");
    }

    @FXML
    private void cancelCurrency() {
        currencyChoiceBox.setValue(userData.getCurrencyCode());
        currencyChoiceBox.getStyleClass().removeAll("ChangedValue");
    }

    @FXML
    private void addConnection() {
        userData.setSelectedURL(urlTextField.getText());
        //update saved URLS
        urlList.setItems(FXCollections.observableArrayList(userData.getUrlList()));
        urlList.setValue(urlTextField.getText());
        selectedURLAvailabiltyStyle();
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
        selectedURLAvailabiltyStyle();
    }

    @FXML
    private void removeSavedConnection() {
        String target = urlList.getValue();
        userData.removeUrl(target);
        urlList.setItems(FXCollections.observableArrayList(userData.getUrlList())); //refresh
        urlList.setValue(userData.getServerURL());
        selectedURLAvailabiltyStyle();
    }

    //utility methods
    private void valueChanged(Control choiceBox, String oldVal, String newVal) {
        if (!newVal.equals(oldVal))
            choiceBox.getStyleClass().add("ChangedValue"); //value was changed
        else
            choiceBox.getStyleClass().removeAll("ChangedValue"); //value no longer changed
    }

    private void urlTextChanged() {
        //restore all fields to default
        statusText.setText("N/A");
        statusText.setFill(Color.BLACK);
    }

    private void selectedURLAvailabiltyStyle() {
        urlList.getStyleClass().removeAll("Available", "Unavailable", "ChangedValue");
        urlList.getStyleClass().add(reach(urlList.getValue()) ? "Available" : "Unavailable");
    }

    private boolean reach(String url) {
        try {
            return server.reach(url).getFamily().equals(Response.Status.Family.SUCCESSFUL);
        } catch (ProcessingException e) {
            return false;
        }
    }

    private class UrlListCell extends ListCell<String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) return;

            setText(item);
            try {
                if (reach(item)) {
                    getStyleClass().add("Available");
                    getStyleClass().removeAll("Unavailable");
                    return;
                }
            } catch (ProcessingException e) {}
            getStyleClass().add("Unavailable");
            getStyleClass().removeAll("Available");
        }
    }
}
