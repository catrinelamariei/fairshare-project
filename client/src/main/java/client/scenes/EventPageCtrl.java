package client.scenes;

import client.MainCtrl;
import client.UserData;
import client.utils.ServerUtils;
import commons.DTOs.EventDTO;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ResourceBundle;

public class EventPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private String eventUUID;

    private EventDTO eventDTO;

    private Stage stage;
    private String serverUrl;

    @Inject
    public EventPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Leave this method empty; we'll initialize the scene when it's shown
    }

    public void loadEvent() {
        UserData data = UserData.getInstance();
        this.serverUrl = data.getServerUrl();
        this.eventUUID = data.getCurrentUUID();
        this.eventDTO = getEventDTO();
        System.out.println(eventDTO);
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAA\n\n\n\n");
    }

    public void gotoHome() {
        mainCtrl.showStartPage();
    }

    public void gotoAdminLogin() {
        mainCtrl.showAdminCheckPage();
    }

    public void copyInviteCode() {
        //get system singleton of clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        //get invite code (String)
        String eventName = "NewYearEvent"; //temporary placeholder (owner)

        //copy data to clipboard
        StringSelection content = new StringSelection(this.eventUUID);
        StringSelection owner = new StringSelection(eventName);
        clipboard.setContents(content, owner);

        //display copied for 3 seconds
        System.out.println("Copied to clipboard!"); //temporary placeholder
    }

    public EventDTO getEventDTO(){
        RestTemplate restTemplate = new RestTemplate();
        String url = serverUrl + "/api/event/" +this.eventUUID;
        System.out.println(url);
        try{
            return restTemplate.getForObject(url, EventDTO.class);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
