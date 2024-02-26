package client.scenes;

import client.MainCtrl;
import client.utils.ServerUtils;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;


public class PrivCheckPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    public PasswordField password;
    public Text text;

    @Inject
    public PrivCheckPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        text = new Text();
    }

    public void initialize(URL location, ResourceBundle resources) {}

    public void login() {
        String response = PostRequest(password.getText());

        if(response.equals("Token")) {
            adminPage();
        }else{
            text.setStyle("-fx-text-fill: red;");
            text.setText("Wrong code");
        }
    }

    public void adminPage(){
        mainCtrl.showAdminPage();
    }



    public void RequestCodeGeneration(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/privCheck";
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("Response: " + response);
    }
    public String PostRequest(String code) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/privCheck";
        ResponseEntity<String> response = restTemplate.postForEntity(url, code, String.class);
        return response.getBody();
    }
}

