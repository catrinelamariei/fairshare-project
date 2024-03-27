package client.scenes;

import client.MainCtrl;
import client.UserData;
import client.utils.ServerUtils;
import commons.DTOs.EventDTO;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.UUID;

public class StartPageCtrl {
    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;
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


    @Inject
    public StartPageCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    public void initialize() {
        UserData data = UserData.getInstance();
        updateCurrentEventsVBox(data);
    }

    public void updateCurrentEventsVBox(UserData data) {
        recentEventsVBox.getChildren().clear();
        for (UUID uuid : data.getRecentUUIDs()) {
            Hyperlink eventLink = new Hyperlink(serverUtils.getEvent(uuid).getName());
            eventLink.setOnAction(event -> {
                data.setCurrentUUID(uuid);
                recentEventsVBox.getChildren().remove(eventLink);
                recentEventsVBox.getChildren().add(0, eventLink);
                eventPage();
            });
            recentEventsVBox.getChildren().add(eventLink);
        }
    }

    public void onCreateEvent() {
        String text = newEvent.getText();
        EventDTO e;

        try {
            if (text == null || text.isEmpty()) throw new IllegalArgumentException();
            e = serverUtils.postEvent(new EventDTO(null, text));
        } catch (IllegalArgumentException ex) {
            MainCtrl.alert("Please enter a valid event name.");
            return;
        } catch (WebApplicationException ex) {
            MainCtrl.alert(ex.getMessage());
            return;
        }

        newEvent.clear();
        UserData data = UserData.getInstance();
        data.setCurrentUUID(e.getId());
        updateCurrentEventsVBox(data);
        //confirmation dialog
        MainCtrl.inform(text + " event created!");
        mainCtrl.showEventPage();
    }

    public void onJoinEvent() {
        String text = joinedEvent.getText();
        if (text != null && !text.isEmpty()) {
            try{
                // fetch event to make sure event exists
                // when setting current UUID we pass (id, name) which involves calling getevent
                // if event is not present we throw error, otherwise we save pair

                System.out.println(text + " Event joined");

                UserData data = UserData.getInstance();
                data.setCurrentUUID(UUID.fromString(text));
                updateCurrentEventsVBox(data);

                joinedEvent.clear();
                eventPage();
            }catch(IllegalArgumentException ex){
                MainCtrl.alert("Event not found. Please enter a valid event code.");
            }

        } else {
            // Display an error message if the input is invalid
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid event name.");
            alert.showAndWait();
        }
    }


    public void eventPage() {
        mainCtrl.showEventPage();
    }

    public void adminPage() {
        mainCtrl.showAdminPage();
    }
}
