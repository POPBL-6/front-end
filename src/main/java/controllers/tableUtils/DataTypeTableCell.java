package controllers.tableutils;

import javafx.scene.control.TableCell;
import model.datatypes.Topic;

/**
 * Table Cell to format the topic datatypes in the TopicTable.
 * @author Gorka Olalde
 */
public class DataTypeTableCell extends TableCell<Topic, Object> {

    /**
     * Updates the table cell to display the type of object received.
     * It simply gets the class of the object and displays it's simplified name.
     * It's done this way to simplify the name as the
     * getClass().toString() method displays the full name of the class.
     * @param item Item to get the type from.
     * @param empty If the cell is empty or not.
     */
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

