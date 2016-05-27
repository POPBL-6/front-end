package controllers.tableutils;

import javafx.scene.control.TableCell;
import model.Topic;

/**
 * Table Cell to format the topic datatypes in the TopicTable.
 * @author Gorka Olalde
 */
public class DataTypeTableCell extends TableCell<Topic, Object> {

        @Override
        protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if(item == null || empty) {
            setText("");
        } else {
            if (item instanceof String) {
                setText("String");
            } else if (item instanceof Integer) {
                setText("Integer");
            } else if (item instanceof Boolean) {
                setText("Boolean");
            } else if (item instanceof Double) {
                setText("Double");
            } else {
                setText("Unknown class");
            }
        }
    }
}

