package client;

import client.scenes.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    private PrivCheckPageCtrl privCheckPageCtrl;
    private Scene privCheckPage;
    private TransactionPageCtrl transactionPageCtrl;
    private Scene transactionPage;



    public void initialize(Stage primaryStage, Pair<StartPageCtrl, Parent> startPage,
                           Pair<EventPageCtrl, Parent> eventPage,
                           Pair<AdminPageCtrl, Parent> adminPage,
                           Pair<PrivCheckPageCtrl, Parent> privCheckPage,
                           Pair<StartPageCtrl, Parent> page,
                           Pair<TransactionPageCtrl, Parent> transactionPage) {

        this.primaryStage = primaryStage;

        this.startPageCtrl = startPage.getKey();
        this.startPage = new Scene(startPage.getValue());

        this.eventPageCtrl = eventPage.getKey();
        this.eventPage = new Scene(eventPage.getValue());

        this.privCheckPageCtrl = privCheckPage.getKey();
        this.privCheckPage = new Scene(privCheckPage.getValue());

        this.adminPageCtrl = adminPage.getKey();
        this.adminPage = new Scene(adminPage.getValue());

        this.transactionPageCtrl = transactionPage.getKey();
        this.transactionPage = new Scene(transactionPage.getValue());

        showStartPage();
        primaryStage.show();
    }

    public void showStartPage() {
        primaryStage.setTitle("Home Screen");
        primaryStage.setScene(startPage);
    }

    public void showEventPage() {
        primaryStage.setTitle("event");
        eventPageCtrl.load(); //INITIALIZE eventPage with data
        primaryStage.setScene(eventPage);
    }

    public void showAdminPage() {
        primaryStage.setTitle("<EventName>: admin panel");
        adminPageCtrl.load();
        primaryStage.setScene(adminPage);
    }

    public void showAdminCheckPage() {
        primaryStage.setTitle("<EventName>: admin panel login");
        primaryStage.setScene(privCheckPage);
    }

    public void showTransactionPage() {
        primaryStage.setTitle("<EventName>: overview");
        primaryStage.setScene(transactionPage);
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
