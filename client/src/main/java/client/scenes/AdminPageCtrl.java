package client.scenes;

import client.MainCtrl;
import client.UserData;
import client.utils.ServerUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminPageCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private String token;

    @Inject
    public AdminPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void homePage() {
        mainCtrl.showStartPage();
    }

    public void sendGetRequest() {
        String url = "http://127.0.0.1:8080/data";


        UserData data = UserData.getInstance();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + data.getToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String response = responseEntity.getBody();

        System.out.println("Response from server: " + response);
    }
}
