package client;

import client.scenes.*;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private static ResourceBundle languages;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale locale = Locale.getDefault(); // Get default locale
        loadLanguages(locale);

        // Your existing initialization code
        var startPage = FXML.load(StartPageCtrl.class, languages, locale,
                "client", "scenes", "StartPage.fxml");
        var mainPage = FXML.load(StartPageCtrl.class, languages, locale,
                "client", "scenes", "StartPage.fxml");
        var eventPage = FXML.load(EventPageCtrl.class, languages, locale,
                "client", "scenes", "EventPage.fxml");
        var adminPage = FXML.load(AdminPageCtrl.class, languages, locale,
                "client", "scenes", "AdminPage.fxml");
        var privCheckPage = FXML.load(PrivCheckPageCtrl.class, languages, locale,
                "client", "scenes", "PrivCheckPage.fxml");
        var transactionPage = FXML.load(TransactionPageCtrl.class, languages, locale,
                "client", "scenes", "TransactionPage.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, mainPage, eventPage, adminPage,
                privCheckPage, startPage, transactionPage);

        primaryStage.show();
    }

    // Method to load messages from all language files in the specified directory
    public static void loadLanguages(Locale locale) {
        List<ResourceBundle> bundles = loadAllLanguages();

        // Search for the correct bundle based on the specified locale
        for (ResourceBundle bundle : bundles) {
            if (bundle.getLocale().equals(locale)) {
                languages = bundle;
                return;
            }
        }

        // If no matching bundle found, use the default properties
        languages = ResourceBundle.getBundle("client.lang.EN", Locale.ENGLISH);
    }

    public static List<ResourceBundle> loadAllLanguages() {
        List<ResourceBundle> bundles = new ArrayList<>();
        File langFolder = new File("src/main/resources/client/lang/");
        File[] files = langFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".properties"));

        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                String languageCode = fileName.substring(0, fileName.lastIndexOf('.'));
                ResourceBundle bundle = loadLanguages(languageCode);
                bundles.add(bundle);
            }
        }
        return bundles;
    }

    public static ResourceBundle loadLanguages(String languageCode) {
        Locale locale = new Locale(languageCode);
        return ResourceBundle.getBundle("client.lang." + languageCode, locale);
    }
}
