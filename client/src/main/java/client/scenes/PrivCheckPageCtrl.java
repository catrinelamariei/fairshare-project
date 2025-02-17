package client.scenes;

import client.*;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.core.Response;
import javafx.scene.control.PasswordField;
import javafx.scene.input.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;


public class PrivCheckPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final UserData userData;

    public PasswordField password;
    public Text text = new Text();

    @Inject
    public PrivCheckPageCtrl(ServerUtils server, MainCtrl mainCtrl, UserData userData) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.userData = userData;
    }

    public void initialize(URL location, ResourceBundle resources) {
        password.setOnKeyPressed(this::handleEnterPressed);
    }

    public void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            login();
        }
    }

    public void login() {
        String passwordText = password.getText();
        if(passwordText!=null && !passwordText.isEmpty()){
            Response response = server.adminReqToken(passwordText);

            if(response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                userData.setToken(response.readEntity(String.class));
                adminPage();
            }else{
                MainCtrl.alert(String.format(Main.getTranslation("wrong_code") + " [%d]",
                        response.getStatus()));
            }
        }else{
            MainCtrl.alert(Main.getTranslation("provide_password"));
        }

    }

    public void adminPage(){
        mainCtrl.showAdminPage();
    }

    public void goToHome(){
        mainCtrl.showStartPage();
    }

    public void requestCodeGeneration(){
        server.adminReqCode();
    }
}

