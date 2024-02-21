package client;

import client.scenes.AdminPageCtrl;
import client.scenes.EventPageCtrl;
import client.scenes.StartPageCtrl;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private StartPageCtrl startPageCtrl;
    private Scene startPage;

    private EventPageCtrl eventPageCtrl;
    private Scene eventPage;

    private AdminPageCtrl adminPageCtrl;
    private Scene adminPage;


    public void initialize(Stage primaryStage, Pair<StartPageCtrl, Parent> startPage,
                           Pair<EventPageCtrl, Parent> eventPage,
                           Pair<AdminPageCtrl, Parent> adminPage) {

        this.primaryStage = primaryStage;

        this.startPageCtrl = startPage.getKey();
        this.startPage = new Scene(startPage.getValue());

        this.eventPageCtrl = eventPage.getKey();
        this.eventPage = new Scene(eventPage.getValue());

        this.adminPageCtrl = adminPage.getKey();
        this.adminPage = new Scene(adminPage.getValue());

        showStartPage();
        primaryStage.show();
    }

    public void showStartPage() {
        primaryStage.setTitle("Home Screen");
        primaryStage.setScene(startPage);
    }

    public void showEventPage() {
        primaryStage.setTitle("<EventName>: overview");
        primaryStage.setScene(eventPage);
    }

    public void showAdminPage() {
        primaryStage.setTitle("<EventName>: admin panel");
        primaryStage.setScene(adminPage);
    }
}
