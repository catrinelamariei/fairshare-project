package client.scenes;

import client.MainCtrl;
import client.UserData;
import client.utils.ServerUtils;

import javax.inject.Inject;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ResourceBundle;

public class EventPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final String eventUUID;

    @Inject
    public EventPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        UserData data = UserData.getInstance();
        this.eventUUID = data.getCurrentUUID();
    }

    public void initialize(URL location, ResourceBundle resources) {}

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
}
