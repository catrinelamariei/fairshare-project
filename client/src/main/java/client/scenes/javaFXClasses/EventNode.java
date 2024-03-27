package client.scenes.javaFXClasses;

import client.MainCtrl;
import client.UserData;
import client.utils.ServerUtils;
import commons.DTOs.EventDTO;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
public class EventNode extends TitledPane {
    private final Pair<UUID, String> idNamePair; // for UserData
    private final MainCtrl mainCtrl;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private TextField idField, dateField, participantField, transactionField, balanceField;

    /**
     * create eventnode but without any actual data
     */
    private EventNode(Pair<UUID, String> pair, MainCtrl mainCtrl) {
        super();
        this.idNamePair = pair; //necessary for field to be final
        this.mainCtrl = mainCtrl;
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
        idField.setMaxWidth(245.0d);
        dateField.setMaxWidth(70.0d);
        participantField.setMaxWidth(30.0d);
        transactionField.setMaxWidth(30.0d);
        balanceField.setMaxWidth(60.0d);

        //buttons
        Button joinButton = new Button("JOIN");
        Button deleteButton = new Button("DELETE");
        joinButton.setOnAction(this::join);
        deleteButton.setOnAction(this::delete);

        //gridpance css styling
        GridPane gridPane = new GridPane(10.0d, 0.0d); //gaps between cells
        gridPane.getStyleClass().add("EventNode");

        //gridpane (col/row constraints)
        ColumnConstraints col0 = new ColumnConstraints();
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col0.setHalignment(HPos.RIGHT);
        col1.setHalignment(HPos.LEFT);
        col2.setHalignment(HPos.CENTER);
        gridPane.getColumnConstraints().addAll(col0, col1, col2, col2);
        gridPane.getColumnConstraints().forEach(cc -> cc.setHgrow(Priority.SOMETIMES));

        RowConstraints rowConstr = new RowConstraints();
        rowConstr.setMinHeight(10d);
        rowConstr.setPrefHeight(30d);
        gridPane.getRowConstraints().addAll(rowConstr, rowConstr, rowConstr, rowConstr, rowConstr);

        //gridpane (contents)
        gridPane.addColumn(0, idText, dateText, participantText, transactionText, balanceText);
        gridPane.addColumn(1, idField, dateField, participantField, transactionField, balanceField);
        gridPane.add(joinButton, 2, 0, 2, 2); //button (span 2)
        gridPane.add(deleteButton, 2, 3, 2, 2); //button (span 2)

        //finalize (add gridpane to this)
        this.setContent(gridPane);
    }

    /**
     * create eventnode from data
     * @param event data source
     */
    public EventNode(EventDTO event, MainCtrl mainCtrl) {
        this(new Pair<>(event.getId(), event.getName()), mainCtrl);

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

    private void delete(ActionEvent actionEvent) {
        ((Accordion) this.getParent()).getPanes().remove(this);
        (new ServerUtils()).deleteEvent(idNamePair.getKey());
    }
}
