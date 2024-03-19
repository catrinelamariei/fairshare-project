package client.scenes;

import client.MainCtrl;
import client.UserData;
import client.scenes.javaFXClasses.EventNode;
import client.utils.ServerUtils;
import commons.DTOs.EventDTO;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.UUID;

public class AdminPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private Accordion eventAccordion;

    @Inject
    public AdminPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    //run on startup
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * called when switching to this scene
     */
    public void load() {
        //get all events and insert into accordion
        Collection<EventDTO> events = server.getAllEvents(UserData.getInstance().getToken());
        eventAccordion.getPanes().addAll(events.stream().map(EventNode::new).toList());
    }

    public void homePage() {
        mainCtrl.showStartPage();
    }

    //EVENT-Gen
    public void generateEventTest() {
        Event event = new Event("TestEvent");
        event.id = new UUID(0, 0);
        eventAccordion.getPanes().add(new EventNode(new EventDTO(event)));
    }

    //JSON
    public void sendGetRequest() {
        String url = "http://localhost:8080/data";


        UserData data = UserData.getInstance();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + data.getToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String response = responseEntity.getBody();

        System.out.println("Response from server: " + response);
    }
}
