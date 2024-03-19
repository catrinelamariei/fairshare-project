package client.scenes;
import client.MainCtrl;
import client.UserData;
import client.utils.ServerUtils;

import javax.inject.Inject;

import commons.DTOs.EventDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.UUID;

public class StartPageCtrl {
    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;
    private ArrayList eventIds = new ArrayList<Long>();
    @FXML
    private Button createButton;
    @FXML
    private Button joinButton;
    @FXML
    private TextField newEvent;
    @FXML
    private TextField joinedEvent;
    @FXML
    private Hyperlink eventA;
    @FXML
    private Hyperlink eventB;
    @FXML
    private Hyperlink eventC;

    @Inject
    public StartPageCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }
    public void onCreateEvent() {
        String text = newEvent.getText();
        if (text != null && !text.isEmpty()) {
            EventDTO e = new EventDTO(null,text);
            e = serverUtils.addEvent(e);
            eventIds.add(e.getId());
            newEvent.clear();
            UserData.getInstance().setCurrentUUID(e.getId());
            UserData.getInstance().getRecentUUIDs().add(e.getId());
            //confirmation dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Event Created");
            alert.setHeaderText(null);
            alert.setContentText(text + " Event Created!");
            alert.showAndWait();
        } else {
            // Display an error message if the input is invalid
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid event name.");
            alert.showAndWait();
        }

    }

    //this method still needs work done
    //because it doesn't do anything after you press join
    public void onJoinEvent(){
        String text = joinedEvent.getText();
        if (text != null && !text.isEmpty()) {
            System.out.println(text + " Event joined");
            UserData data = UserData.getInstance();
            data.setCurrentUUID(UUID.fromString(text));
            joinedEvent.clear();
            eventPage();
        } else {
            // Display an error message if the input is invalid
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid event name.");
            alert.showAndWait();
        }
    }

    public void onViewEvent(){
        System.out.println("Redirecting to ");
    }

    public void eventPage() {
        mainCtrl.showEventPage();
    }
    public void adminPage() {
        mainCtrl.showAdminCheckPage();
    }
}
