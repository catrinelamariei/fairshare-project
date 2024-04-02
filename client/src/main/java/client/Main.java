package client;

import client.scenes.*;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.google.inject.Guice.createInjector;


public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private static final ResourceBundle messages = ResourceBundle.getBundle("client.Languages.EN");

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Locale locale = Locale.getDefault(); // Get default locale
        var startPage = FXML.load(StartPageCtrl.class, messages, locale,
                "client", "scenes", "StartPage.fxml");
        var mainPage = FXML.load(StartPageCtrl.class, messages, locale,
                "client", "scenes", "StartPage.fxml");
        var eventPage = FXML.load(EventPageCtrl.class, messages, locale,
                "client", "scenes", "EventPage.fxml");
        var adminPage = FXML.load(AdminPageCtrl.class, messages, locale,
                "client", "scenes", "AdminPage.fxml");
        var privCheckPage = FXML.load(PrivCheckPageCtrl.class, messages, locale,
                "client",
                "scenes", "PrivCheckPage.fxml");
        var transactionPage = FXML.load(TransactionPageCtrl.class, messages, locale,
                "client",
                "scenes", "TransactionPage.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, mainPage, eventPage, adminPage,
                privCheckPage, startPage, transactionPage);
    }
}
