package client.scenes;
import client.utils.ServerUtils;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javax.inject.Inject;
import java.util.ArrayList;

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
            Event e = new Event(text);
            serverUtils.addEvent(e);
            eventIds.add(e.getId());
            newEvent.clear();
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
            joinedEvent.clear();
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


}
