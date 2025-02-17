package client.scenes;

import client.*;
import client.scenes.javaFXClasses.NodeFactory;
import client.scenes.javaFXClasses.VisualNode.VisualEventNode;
import client.utils.*;
import com.google.inject.Inject;
import commons.DTOs.EventDTO;
import commons.Event;
import jakarta.ws.rs.NotAuthorizedException;
import javafx.application.Platform;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

import static javafx.collections.FXCollections.observableArrayList;

public class AdminPageCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final NodeFactory nodeFactory;
    private final EventJsonUtil jsonUtil;
    private boolean ascending = true;

    //overview management
    @FXML
    private Accordion eventAccordion;
    @FXML
    private Tab overviewTab;
    @FXML
    private ChoiceBox<EventDTO.EventComparator> comparatorList;
    @FXML
    private Button ascDescButton;
    @FXML
    private HBox sortingContainer;

    @Inject
    public AdminPageCtrl(ServerUtils server, MainCtrl mainCtrl, NodeFactory nodeFactory,
                         EventJsonUtil jsonUtil) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.nodeFactory = nodeFactory;
        this.jsonUtil = jsonUtil;
    }

    //run on startup
    public void initialize(URL location, ResourceBundle resources) {
        comparatorList.setItems(observableArrayList(EventDTO.EventComparator.values()));
        comparatorList.setValue(EventDTO.EventComparator.name);
        comparatorList.valueProperty().addListener(((ov, oldVal, newVal) -> reSort()));
        overviewTab.selectedProperty().addListener(((ov, oldVal, newVal) -> toggleTab(newVal)));
    }

    private void subscribe() {
        server.register("/topic/events", (Consumer<EventDTO>) q -> {
            System.out.println("Received event update");

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //remove old event
                    removeEventNode(q);

                    eventAccordion.getPanes().add(nodeFactory.createEventNode(q));
                }
            });


        });

        server.register("/topic/events", (Consumer<UUID>) q -> {
            System.out.println("Received event update");

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    EventDTO event = server.getEvent(q);
                    removeEventNode(event);

                    eventAccordion.getPanes().add(nodeFactory.createEventNode(event));
                }
            });


        },null);

        server.register("/topic/deletedEvent", (Consumer<EventDTO>) q -> {
            System.out.println("Received event delete");

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    removeEventNode(q);
                }
            });


        });
    }

    private void removeEventNode(EventDTO event) {
        eventAccordion.getPanes().removeIf(p -> {
            if (p instanceof VisualEventNode) {
                return ((VisualEventNode) p).getPair().getKey().equals(event.id);
            }
            return false;

        });
    }

    /**
     * called when switching to this scene
     */
    public void load() throws NotAuthorizedException {
        //contact the server, if unauthorized return immediately
        ArrayList<EventDTO> events = new ArrayList<>(server.getAllEvents()); //get all events

        server.webSocketReconnect();
        subscribe();
        //get desired sorting order
        Comparator<EventDTO> cmp = comparatorList.getValue().cmp;
        if (!ascending) cmp = cmp.reversed();

        events.sort(cmp); //sort
        eventAccordion.getPanes().setAll(events.stream()
                .map(nodeFactory::createEventNode).toList());
    }

    public void homePage() {
        mainCtrl.showStartPage();
    }

    //EVENT-Gen
    public void generateEventTest() {
        Event event = new Event("TestEvent");
        event.id = new UUID(0, 0);
        eventAccordion.getPanes().add(nodeFactory.createEventNode(new EventDTO(event)));
    }

    //EVENT-ordering
    public void reSort() {
        //this can be optimized later,
        // for now we simply refresh the entire page with new desired sorting order
        load();
    }

    public void toggleAscDesc() {
        ascending = !ascending;
        ascDescButton.setText(ascending ? Main.getTranslation("ascending")
                : Main.getTranslation("descending"));
        reSort();
    }

    /**
     * toggle extra sort-fields when closing/opening overview tab
     */
    private void toggleTab(boolean opened) {
        sortingContainer.setManaged(opened);
    }

    //JSON
    public void sendGetRequest() {
        String response = server.getJSON();
        System.out.println("Response from server: " + response);
    }

    @FXML
    private void uploadJson() throws IOException {
        //get file
        FileChooser fileCHooser = new FileChooser();
        fileCHooser.setTitle(Main.getTranslation("load_json"));
        FileChooser.ExtensionFilter extensionFilter =
            new FileChooser.ExtensionFilter("JSON", "*.json");
        fileCHooser.getExtensionFilters().add(extensionFilter);
        File file = fileCHooser.showOpenDialog(mainCtrl.primaryStage);

        //get JSON and turn into DTO
        String out = (new Scanner(file)).useDelimiter("\\Z").next();
        EventDTO event = jsonUtil.putJSON(out);

        //update nodes
        eventAccordion.getPanes().removeIf(titledPane ->
            ((titledPane instanceof VisualEventNode ven)
            && ven.getPair().getKey().equals(event.id)));
        eventAccordion.getPanes().add(nodeFactory.createEventNode(event));
    }
}
