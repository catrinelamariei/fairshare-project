package client;

import client.scenes.*;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ResourceBundle;

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

        primaryStage.onCloseRequestProperty().set(e -> {
            try {
                INJECTOR.getInstance(ServerUtils.class).stop();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        primaryStage.show();
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
        var settingsPage = FXML.load(SettingsPageCtrl.class, languageBundle,
                    "client", "scenes", "SettingsPage.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, mainPage, eventPage, adminPage,
                privCheckPage, startPage, settingsPage);
    }

    public static ResourceBundle loadLanguages(String languageCode) {
        return ResourceBundle.getBundle("client.lang." + languageCode);
    }

    public static String getTranslation(String variable){
        return languageBundle.getString(variable);
    }
    //example usage: main.getTranslation("email")
}
