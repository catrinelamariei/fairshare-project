package client.scenes;

import client.MainCtrl;
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
    private final Integer eventId;

    @Inject
    public EventPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.eventId = 1; //temporary placeholder
    }

    public void initialize(URL location, ResourceBundle resources) {}

    public void gotoHome() {
        mainCtrl.showStartPage();
    }
    public void gotoAdminLogin() {
        mainCtrl.showAdminPage();
    }

    public void copyInviteCode() {
        //get system singleton of clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        //get invite code (String)
        String invCode = "123456789"; //temporary placeholder (content)
        String eventName = "NewYearEvent"; //temporary placeholder (owner)

        //copy data to clipboard
        StringSelection content = new StringSelection(invCode);
        StringSelection owner = new StringSelection(eventName);
        clipboard.setContents(content, owner);

        //display copied for 3 seconds
        System.out.println("Copied to clipboard!"); //temporary placeholder
    }
}
