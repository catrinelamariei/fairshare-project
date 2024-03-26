package client.scenes;

import client.MainCtrl;
import client.scenes.javaFXClasses.EventNode;
import client.utils.ServerUtils;
import commons.DTOs.EventDTO;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;

import javax.inject.Inject;
import java.net.URL;
import java.util.*;

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
        Collection<EventDTO> events = server.getAllEvents();

        List<EventNode> list = new ArrayList<>();
        for (EventDTO event : events) {
            EventNode eventNode = new EventNode(event);
            list.add(eventNode);
        }
        eventAccordion.getPanes().addAll(list);
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
        String response = server.getJSON();
        System.out.println("Response from server: " + response);
    }
}
