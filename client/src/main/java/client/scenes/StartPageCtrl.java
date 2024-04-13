package client.scenes;

import client.*;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.DTOs.EventDTO;
import jakarta.ws.rs.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.File;
import java.util.*;

import static client.UserData.Pair;

public class StartPageCtrl {
    //services
    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final Main main;
    private final UserData userData;

    //FXML
    @FXML
    private Button createButton;
    @FXML
    private Button joinButton;
    @FXML
    private TextField newEvent;
    @FXML
    private TextField joinedEvent;
    @FXML
    private VBox recentEventsVBox;
    @FXML
    public Region veil;


    @Inject
    public StartPageCtrl(ServerUtils serverUtils, MainCtrl mainCtrl, Main main, UserData userData) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
        this.main = main;
        this.userData = userData;
    }

    @FXML
    public void initialize() {

        //event links
        recentEventsVBox.getChildren().setAll(userData.getRecentUUIDs()
            .stream().map(EventHyperlink::new).toList());
    }



    public void onCreateEvent() {
        String text = newEvent.getText();
        EventDTO e;

        try {
            if (text == null || text.isEmpty()) throw new IllegalArgumentException();
            e = serverUtils.postEvent(new EventDTO(null, text));
        } catch (IllegalArgumentException ex) {
            MainCtrl.alert(Main.getTranslation("invalid_event_name"));
            return;
        } catch (WebApplicationException ex) {
            MainCtrl.alert(ex.getMessage());
            return;
        }catch (ProcessingException ex){
            MainCtrl.alert(Main.getTranslation("server_not_available"));
            return;
        }

        newEvent.clear();

        //add to hyperlinks
        Pair<UUID, String> pair = new Pair<>(e.getId(), e.getName());
        setCurrentEvent(pair);

        //confirmation dialog
        MainCtrl.inform(Main.getTranslation("event"),
                Main.getTranslation("event_creation_start")
                + text + Main.getTranslation("event_creation_end"));
        mainCtrl.showEventPage();
    }

    /**
     * sets the current event to specified id and adds it to the top of recentEventsVBox
     * @param pair pair of ID (of event) and name (local stored to use if event is deleted)
     */
    private void setCurrentEvent(Pair<UUID, String> pair) {
        userData.setCurrentUUID(pair);
        Optional<Node> hyperlinkMatch = recentEventsVBox.getChildren().stream()
            .filter(ehl -> ((EventHyperlink) ehl).pair.getKey().equals(pair.getKey())).findFirst();
        if (hyperlinkMatch.isPresent()) { //hyperlink already present? move to top
            recentEventsVBox.getChildren().remove(hyperlinkMatch.get());
            recentEventsVBox.getChildren().add(0, hyperlinkMatch.get());
        } else {//create new at top
            recentEventsVBox.getChildren().add(0, new EventHyperlink(pair));
        }
    }

    public void deleteRecentEvent(UUID id) {
        recentEventsVBox.getChildren()
                .removeIf(ehl -> ((EventHyperlink) ehl).pair.getKey().equals(id));
    }

    public void onJoinEvent() {
        String text = joinedEvent.getText();
        if (text != null && !text.isEmpty()) {
            try{
                EventHyperlink ehl = new EventHyperlink(new Pair<>(UUID.fromString(text), ""));
                if (ehl.isDisable()) throw new NotFoundException(); //event was not found

                joinedEvent.clear();
                System.out.println(ehl.pair.getValue() + " Event joined");
                recentEventsVBox.getChildren().add(ehl);
                userData.setCurrentUUID(ehl.pair);
                eventPage();
            }catch(NotFoundException e){
                MainCtrl.alert(Main.getTranslation("event_not_found_2"));
            } catch (IllegalArgumentException e) {
                MainCtrl.alert(String.format(
                        Main.getTranslation("invite_code_error") + "\n[%s]", text));
            }

        } else {
            // Display an error message if the input is invalid
            MainCtrl.alert(Main.getTranslation("event_not_found_3"));
        }
    }


    public void eventPage() {
        mainCtrl.showEventPage();
    }

    public void adminPage() {
        mainCtrl.showAdminPage();
    }

    public void settingsPage() {
        mainCtrl.showSettingsPage();
    }

    private class EventHyperlink extends Hyperlink {
        public Pair<UUID, String> pair;

        /**
         * creates a hyperlink from pair, gets the name from the server,
         * if the event is not found, it is replaced with a strikethrough
         * hyperlink with the original name
         * @param p with eventID and original name
         */
        private EventHyperlink(Pair<UUID, String> p) {
            super();
            try {
                this.pair = new Pair<>(p.getKey(), serverUtils.getEvent(p.getKey()).getName());
                this.setOnAction(event -> {
                    userData.setCurrentUUID(this.pair);
                    recentEventsVBox.getChildren().remove(this);
                    recentEventsVBox.getChildren().add(0, this);
                    eventPage();
                });
            } catch (NotFoundException | ProcessingException e) {
                this.pair = new Pair<>(p.getKey(), p.getValue());
                this.getStyleClass().add("dissabledHyperlink");
                this.setDisable(true); //cant be clicked on
            }
            this.setText(this.pair.getValue());
        }
    }

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    public static List<String> getAllLanguageCodes(){
        String folderPath1 = "src/main/resources/client/lang";
        String folderPath2 = "client/src/main/resources/client/lang";

        List<String> fileNameList = new ArrayList<>();

        // Create a File object representing the folder
        File folder1 = new File(folderPath1);
        File folder2 = new File(folderPath2);

        // Get a list of files in the folder
        File[] files = folder1.listFiles();

        // Add filenames to the list
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String filename = file.getName();
                    filename = filename.substring(0, filename.length()-11);
                    fileNameList.add(filename);
                }
            }
        }

        files = folder2.listFiles();

        // Add filenames to the list
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String filename = file.getName();
                    filename = filename.substring(0, filename.length()-11);
                    fileNameList.add(filename);
                }
            }
        }
        return fileNameList;
    }
}
