package controllers.tableutils;

import javafx.scene.Node;
import javafx.scene.control.*;
import model.datatypes.Topic;

import java.util.Arrays;

/**
 * Cell renderer for the Topic values.
 * @author Gorka Olalde
 */
public class TopicValueTableCell extends TableCell<Topic, Object>{


    /**
     * Updates the cell to show the different types of data in a readable way.
     * Calls the method to render de cell based on the item class.
     * @param item The item to be rendered.
     * @param empty If the cell is empty or not.
     */
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        Node fxControl;
        if(empty || item == null) {
            setText("Unknown");
        } else {
            if (item instanceof String) {
                fxControl = createStringNode((String) item);
            } else if (item instanceof Boolean) {
                fxControl = createBooleanNode((Boolean) item);
            } else if (item instanceof Integer) {
                fxControl = createIntegerNode((Integer) item);
            } else if (item instanceof Double) {
                fxControl = createDoubleNode((Double) item);
            } else {
                fxControl = createUnknownNode(item);
            }
        setGraphic(fxControl);
        }
    }


    /**
     * Method to return a TextField in case that the object is a string.
     * @param value
     * @return
     */
    private Node createStringNode(String value) {
        return new Label(value);
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
