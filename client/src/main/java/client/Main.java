package client;

import client.scenes.*;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    public static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private static ResourceBundle languageBundle;
    private static Stage primaryStage;
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //UserData.getInstance().getLangCode()
        //Locale locale = Locale.getDefault(); // Get default locale
        this.primaryStage = primaryStage;
        initializeUI(UserData.getInstance().getLanguageCode());
    }

    public static void initializeUI(String langCode) {
        languageBundle = loadLanguages(langCode);

        // Your existing initialization code
        var startPage = FXML.load(StartPageCtrl.class, languageBundle,
                "client", "scenes", "StartPage.fxml");
        var mainPage = FXML.load(StartPageCtrl.class, languageBundle,
                "client", "scenes", "StartPage.fxml");
        var eventPage = FXML.load(EventPageCtrl.class, languageBundle,
                "client", "scenes", "EventPage.fxml");
        var adminPage = FXML.load(AdminPageCtrl.class, languageBundle,
                "client", "scenes", "AdminPage.fxml");
        var privCheckPage = FXML.load(PrivCheckPageCtrl.class, languageBundle,
                "client", "scenes", "PrivCheckPage.fxml");
        var transactionPage = FXML.load(TransactionPageCtrl.class, languageBundle,
                "client", "scenes", "TransactionPage.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, mainPage, eventPage, adminPage,
                privCheckPage, startPage, transactionPage);

        primaryStage.show();
    }

    public static ResourceBundle loadLanguages(String languageCode) {
        return ResourceBundle.getBundle("client.lang." + languageCode);
            privCheckPage, startPage, transactionPage);


        primaryStage.setOnCloseRequest(e->{
            eventPage.getKey().stop();
        });
    }

    public static String getTranslation(String variable){
        return languageBundle.getString(variable);
    }
    //example usage: main.getTranslation("email")
}
