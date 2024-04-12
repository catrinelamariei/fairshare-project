package client.scenes.javaFXClasses.VisualNode;

import client.*;
import client.scenes.javaFXClasses.DataNode.EventNode;
import client.utils.EventJsonUtil;
import client.utils.ServerUtils;
import commons.DTOs.EventDTO;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static client.UserData.Pair;
public class VisualEventNode extends EventNode {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private TextField idField, dateField, participantField, transactionField, balanceField;
    private Text invitationCodeText;

    /**
     * create eventnode but without any actual data
     */
    private void initialize() {
        this.setAnimated(false);

        //text
        Text idText = new Text("ID");
        Text dateText = new Text("Creation Date");
        Text participantText = new Text("Participants");
        Text transactionText = new Text("Transactions");
        Text balanceText = new Text("Outstanding Balance");

        //text-fields (empty/no-data)
        idField = new TextField();
        dateField = new TextField();
        participantField = new TextField();
        transactionField = new TextField();
        balanceField = new TextField();

        //text-fields styling
        List.of(idField, dateField, participantField, transactionField, balanceField)
                .forEach(tf -> {tf.setEditable(false); tf.setAlignment(Pos.CENTER);});

        //buttons
        Button joinButton = new Button("JOIN");
        Button downloadButton = new Button("DOWNLOAD");
        Button deleteButton = new Button("DELETE");
        joinButton.setOnAction(this::join);
        downloadButton.setOnAction(this::jsonSave);
        deleteButton.setOnAction(this::delete);

        //gridpance css styling
        GridPane gridPane = new GridPane(10.0d, 10.0d); //gaps between cells
        gridPane.getStyleClass().add("EventNode");

        //gridpane (col/row constraints)
        ColumnConstraints col0 = new ColumnConstraints();
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col0.setHalignment(HPos.RIGHT);
        col1.setPrefWidth(250d);
        col1.setMaxWidth(Double.NEGATIVE_INFINITY);
        col2.setHalignment(HPos.CENTER);
        gridPane.getColumnConstraints().addAll(col0, col1, col2);
        gridPane.getColumnConstraints().forEach(cc -> cc.setHgrow(Priority.SOMETIMES));

        //gridpane (contents)
        gridPane.addColumn(0, idText, dateText, participantText, transactionText, balanceText);
        gridPane.addColumn(1, idField, dateField, participantField, transactionField, balanceField);
        gridPane.add(joinButton, 2, 0, 1, 2); //button (span 2)
        gridPane.add(downloadButton, 2, 1, 1, 3);
        gridPane.add(deleteButton, 2, 3, 1, 2); //button (span 2)

        //finalize (add gridpane to this)
        this.setContent(gridPane);
    }

    /**
     * create eventnode from data
     * @param event data source
     */
    protected VisualEventNode(EventDTO event, MainCtrl mainCtrl, EventJsonUtil jsonUtil) {
        super(mainCtrl, jsonUtil, new Pair<>(event.getId(), event.getName()));
        this.initialize();

        this.setText(event.name);
        idField.setText(event.id.toString());
        dateField.setText(formatter.format(event.date));
        participantField.setText(String.valueOf(event.participants.size()));
        transactionField.setText(String.valueOf(event.transactions.size()));
        balanceField.setText("n/a"); // TODO: insert issue #83
    }

    //buttons
    private void join(ActionEvent actionEvent) {
        UserData.getInstance().setCurrentUUID(idNamePair);
        mainCtrl.showEventPage();
    }

    private void jsonSave(ActionEvent actionEvent) {
        //getting location to save file
        FileChooser fileCHooser = new FileChooser();
        fileCHooser.setTitle(Main.getTranslation("save_json"));
        FileChooser.ExtensionFilter extensionFilter =
            new FileChooser.ExtensionFilter("JSON", "*.json");
        fileCHooser.getExtensionFilters().add(extensionFilter);
        fileCHooser.setSelectedExtensionFilter(extensionFilter);
        File file = fileCHooser.showSaveDialog(mainCtrl.primaryStage);

        //creating and saving file
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(jsonUtil.getJson(idNamePair.getKey()));
            fw.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void delete(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Main.getTranslation("delete_event"));
        alert.setHeaderText(Main.getTranslation("sure_of_deletion"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            ;

            ((Accordion) this.getParent()).getPanes().remove(this);
            (new ServerUtils()).deleteEvent(idNamePair.getKey());

            // TODO: put these lines into service
            mainCtrl.startPageCtrl.deleteRecentEvent(idNamePair.getKey());
            UserData.getInstance().getRecentUUIDs()
                    .removeIf(p -> p.getKey().equals(idNamePair.getKey()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VisualEventNode eventNode)) return false;
        return Objects.equals(idNamePair.getKey(), eventNode.idNamePair.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNamePair);
    }
}
