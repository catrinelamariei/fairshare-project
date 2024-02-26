package client;

import client.scenes.AdminPageCtrl;
import client.scenes.EventPageCtrl;
import client.scenes.PrivCheckPageCtrl;
import client.scenes.StartPageCtrl;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import static com.google.inject.Guice.createInjector;


public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        var startPage = FXML.load(StartPageCtrl.class, "client", "scenes", "StartPage.fxml");
        var mainPage = FXML.load(StartPageCtrl.class, "client", "scenes", "StartPage.fxml");
        var eventPage = FXML.load(EventPageCtrl.class, "client", "scenes", "EventPage.fxml");
        var adminPage = FXML.load(AdminPageCtrl.class, "client", "scenes", "AdminPage.fxml");
        var privCheckPage = FXML.load(PrivCheckPageCtrl.class, "client", "scenes", "PrivCheckPage.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, mainPage, eventPage, adminPage, privCheckPage, startPage);
    }
}
