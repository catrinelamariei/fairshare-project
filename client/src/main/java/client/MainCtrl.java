package client;

import client.scenes.*;
import client.utils.EventPageKeyEventHandler;
import jakarta.ws.rs.NotFoundException;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    public StartPageCtrl startPageCtrl; //this needs to be fixed -> need to start using services
    private Scene startPage;

    private EventPageCtrl eventPageCtrl;
    public Scene eventPage;

    private AdminPageCtrl adminPageCtrl;
    private Scene adminPage;

    private PrivCheckPageCtrl privCheckPageCtrl;
    private Scene privCheckPage;

    private SettingsPageCtrl settingsPageCtrl;
    private Scene settingsPage;

    public void initialize(Stage primaryStage, Pair<StartPageCtrl, Parent> startPage,
                           Pair<EventPageCtrl, Parent> eventPage,
                           Pair<AdminPageCtrl, Parent> adminPage,
                           Pair<PrivCheckPageCtrl, Parent> privCheckPage,
                           Pair<StartPageCtrl, Parent> page,
                           Pair<SettingsPageCtrl, Parent> settingsPage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(windowEvent -> UserData.getInstance().save());

        this.startPageCtrl = startPage.getKey();
        this.startPage = new Scene(startPage.getValue());

        this.eventPageCtrl = eventPage.getKey();
        this.eventPage = new Scene(eventPage.getValue());
        this.eventPage.setOnKeyPressed(new EventPageKeyEventHandler(eventPageCtrl.undoService));

        this.privCheckPageCtrl = privCheckPage.getKey();
        this.privCheckPage = new Scene(privCheckPage.getValue());

        this.adminPageCtrl = adminPage.getKey();
        this.adminPage = new Scene(adminPage.getValue());

        this.settingsPageCtrl = settingsPage.getKey();
        this.settingsPage = new Scene(settingsPage.getValue());

        showStartPage();
        primaryStage.show();
    }

    public void showStartPage() {
        primaryStage.setTitle("Home Screen");
        primaryStage.setScene(startPage);
    }

    public void showEventPage() {
        try {
            eventPageCtrl.load(); //INITIALIZE eventPage with data
            primaryStage.setScene(eventPage);
            primaryStage.setTitle("event");
        } catch (NotFoundException e) {
            MainCtrl.alert("404 - EVENT NOT FOUND!");
        }
    }

    public void showAdminPage() {
        if (UserData.getInstance().getToken() == null) {
            showAdminCheckPage(); //if no key is present, obtain one
            return;
        }

        primaryStage.setTitle("<EventName>: admin panel");
        adminPageCtrl.load();
        primaryStage.setScene(adminPage);
    }

    public void showAdminCheckPage() {
        primaryStage.setTitle("<EventName>: admin panel login");
        primaryStage.setScene(privCheckPage);
    }

    /**
     * creates a new window with settings
     * disables current window
     */
    public void showSettingsPage() {
        Stage sideStage = new Stage();
        sideStage.setScene(settingsPage);
        sideStage.setTitle("Settings");

        startPageCtrl.veil.visibleProperty().bind(sideStage.showingProperty());
        sideStage.initModality(Modality.APPLICATION_MODAL);
        sideStage.showAndWait(); //waits until sideStage is closed
    }

    // Display an error message if the input is invalid
    public static void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void inform(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event Created");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }


}
