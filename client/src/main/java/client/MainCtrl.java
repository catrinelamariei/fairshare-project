package client;

import client.scenes.*;
import client.utils.EventPageKeyEventHandler;
import jakarta.ws.rs.NotFoundException;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
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

    private TransactionPageCtrl transactionPageCtrl;
    private Scene transactionPage;

    public void initialize(Stage primaryStage, Pair<StartPageCtrl, Parent> startPage,
                           Pair<EventPageCtrl, Parent> eventPage,
                           Pair<AdminPageCtrl, Parent> adminPage,
                           Pair<PrivCheckPageCtrl, Parent> privCheckPage,
                           Pair<StartPageCtrl, Parent> page,
                           Pair<TransactionPageCtrl, Parent> transactionPage) {

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

    public static void inform(String entity, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(entity + " Created");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }


}
