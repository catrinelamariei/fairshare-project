package client.scenes.javaFXClasses.VisualNode;

import client.*;
import client.scenes.javaFXClasses.DataNode.EventNode;
import client.utils.ServerUtils;
import commons.DTOs.EventDTO;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

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
    protected VisualEventNode(EventDTO event, MainCtrl mainCtrl) {
        super(mainCtrl, new Pair<>(event.getId(), event.getName()));
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

    private void delete(ActionEvent actionEvent) {
        ((Accordion) this.getParent()).getPanes().remove(this);
        (new ServerUtils()).deleteEvent(idNamePair.getKey());

        // TODO: put these lines into service
        mainCtrl.startPageCtrl.deleteRecentEvent(idNamePair.getKey());
        UserData.getInstance().getRecentUUIDs()
                .removeIf(p -> p.getKey().equals(idNamePair.getKey()));
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
