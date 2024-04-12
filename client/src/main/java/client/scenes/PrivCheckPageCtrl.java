package client.scenes;

import client.*;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import jakarta.ws.rs.core.Response;
import javafx.scene.control.PasswordField;
import javafx.scene.input.*;
import javafx.scene.text.Text;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ResourceBundle;


public class PrivCheckPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final String serverUrl;
    public PasswordField password;
    public Text text;

    @Inject
    public PrivCheckPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        UserData data = UserData.getInstance();
        this.serverUrl = data.getServerURL();
        this.server = server;
        this.mainCtrl = mainCtrl;
        text = new Text();
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
                UserData.getInstance().setToken(response.readEntity(String.class));
                adminPage();
            }else{
                MainCtrl.alert(String.format("ERROR AUTHENTICATING [%d]", response.getStatus()));
            }
        }else{
            MainCtrl.alert("Please provide a password");
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
    public String postRequest(String code) {
        RestTemplate restTemplate = new RestTemplate();
        String url = serverUrl + "/admin";
        ResponseEntity<String> response = restTemplate.postForEntity(url, code, String.class);
        return response.getBody();
    }
}

