package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.awt.*;
import java.util.ArrayList;

public class StartPageCtrl {

    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;

    private ArrayList events = new ArrayList<String>();

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


}
