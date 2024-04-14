package client;

import client.scenes.*;
import client.utils.KeyEvents.EventPageKeyEventHandler;
import client.utils.KeyEvents.PrivCheckPageKeyEventHandler;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.stage.*;
import javafx.util.Pair;

public class MainCtrl {
    //services
    private final UserData userData;

    public Stage primaryStage;

    public StartPageCtrl startPageCtrl; //this needs to be fixed -> need to start using services
    private Scene startPage;

    private EventPageCtrl eventPageCtrl;
    public Scene eventPage;

    private AdminPageCtrl adminPageCtrl;
    private Scene adminPage;

    private PrivCheckPageCtrl privCheckPageCtrl;
    private Scene privCheckPage;

    private final Stage sideStage = new Stage();
    private SettingsPageCtrl settingsPageCtrl;
    private Scene settingsPage;

//    private final ServerUtils server;


    @Inject
    public MainCtrl(UserData userData) {
        this.userData = userData;
        sideStage.initModality(Modality.APPLICATION_MODAL);
        sideStage.setOnCloseRequest(windowEvent -> userData.save());
    }

    public void initialize(Stage primaryStage,
                           Pair<StartPageCtrl, Parent> startPage,
                           Pair<EventPageCtrl, Parent> eventPage,
                           Pair<AdminPageCtrl, Parent> adminPage,
                           Pair<PrivCheckPageCtrl, Parent> privCheckPage,
                           Pair<StartPageCtrl, Parent> page,
                           Pair<SettingsPageCtrl, Parent> settingsPage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(windowEvent -> userData.save());

        this.startPageCtrl = startPage.getKey();
        this.startPage = new Scene(startPage.getValue());

        this.eventPageCtrl = eventPage.getKey();
        this.eventPage = new Scene(eventPage.getValue());
        this.eventPage.setOnKeyPressed(new EventPageKeyEventHandler(eventPageCtrl.undoService));

        this.privCheckPageCtrl = privCheckPage.getKey();
        this.privCheckPage = new Scene(privCheckPage.getValue());
        this.privCheckPage.setOnKeyPressed(
                new PrivCheckPageKeyEventHandler(this.privCheckPageCtrl));

        this.adminPageCtrl = adminPage.getKey();
        this.adminPage = new Scene(adminPage.getValue());

        this.settingsPageCtrl = settingsPage.getKey();
        this.settingsPage = new Scene(settingsPage.getValue());
        sideStage.setScene(this.settingsPage); //update after language switch
        startPageCtrl.veil.visibleProperty().bind(sideStage.showingProperty());

        showStartPage();
        primaryStage.show();
    }

    public void showStartPage() {
        primaryStage.setTitle(Main.getTranslation("home_screen"));
        primaryStage.setScene(startPage);
    }

    public void showEventPage() {
        try {
            eventPageCtrl.load(); //INITIALIZE eventPage with data
            primaryStage.setScene(eventPage);
            primaryStage.setTitle(Main.getTranslation("event"));
        } catch (NotFoundException e) {
            MainCtrl.alert(Main.getTranslation("event_not_found_404"));
        }
    }

    public void showAdminPage() {
        primaryStage.setTitle(Main.getTranslation("admin_panel"));
        try {
            adminPageCtrl.load();
            primaryStage.setScene(adminPage);
        } catch (NotAuthorizedException e) {
            showAdminCheckPage();
        }
    }

    public void showAdminCheckPage() {
        primaryStage.setTitle(Main.getTranslation("admin_panel_login"));
        primaryStage.setScene(privCheckPage);
    }

    /**
     * creates a new window with settings
     * disables current window
     */
    public void showSettingsPage() {
        sideStage.setScene(settingsPage);
        sideStage.setTitle(Main.getTranslation("settings"));

        sideStage.showAndWait(); //waits until sideStage is closed
        startPageCtrl.initialize(); //TODO: check if this is actually sufficient to switch url
    }

    // Display an error message if the input is invalid

    public static void alert(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }


    public static void inform(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
