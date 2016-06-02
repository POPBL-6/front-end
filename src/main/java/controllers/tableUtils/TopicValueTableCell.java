package controllers.tableutils;

import javafx.scene.Node;
import javafx.scene.control.*;
import model.datatypes.Topic;

import java.util.Arrays;

/**
 * Cell renderer for the Topic values.
 * @author Gorka Olalde
 */
class TopicValueTableCell extends TableCell<Topic, Object>{

    protected void updateItem(Topic item, boolean empty) {
        super.updateItem(item, empty);
        Object value = item.getLastValue();
        Node fxControl;
        if(empty) {
            setText("Unknown");
        } else {
            if (value instanceof String) {
                fxControl = createStringNode((String) value);
            } else if (value instanceof Boolean) {
                fxControl = createBooleanNode((Boolean) value);
            } else if (value instanceof Integer) {
                fxControl = createIntegerNode((Integer) value);
            } else if (value instanceof Double) {
                fxControl = createDoubleNode((Double) value);
            } else {
                fxControl = createUnknownNode(value);
            }
            setGraphic(fxControl);
        }
    }



    private Node createStringNode(String value) {
        return new TextField(value);
    }


    private Node createBooleanNode(Boolean value) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.itemsProperty().get().addAll(Arrays.asList("On", "Off"));
        comboBox.getSelectionModel().select(value ? "On" : "Off");

        comboBox.setCellFactory(callback -> new ListCell<String>() {
             @Override
             protected void updateItem(String item, boolean empty) {
                 if ("On".equals(item)) {
                     setStyle("-fx-text-fill: #8bff95");
                 } else {
                     setStyle("-fx-text-fill: #ff4036");
                 }
             }
         });
        return comboBox;
    }

    private Node createIntegerNode(Integer value) {
        TextField field = new TextField();
        field.setText(String.valueOf(value));
        return field;
    }

    private Node createDoubleNode(Double value) {
        TextField field = new TextField();
        field.setText(String.format("%.3f", value));
        return field;
    }

    private Node createUnknownNode(Object value) {
        Label label = new Label();
        label.setText(value.toString());
        return label;
    }

}
