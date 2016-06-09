package controllers.tableutils;

import javafx.scene.control.TableCell;
import model.datatypes.Topic;

import java.text.DateFormat;

/**
 * TableCell to format the timestamps of the Topic.
 * @author Gorka Olalde
 */
public class TimestampTableCell extends TableCell<Topic, Long>{

    /**
     * Updates the table cell to show the format in a readable way.
     * @param item The timestamp to be formatted.
     * @param empty If the cell is empty or not.
     */
    @Override
    protected void updateItem(Long item, boolean empty) {
        super.updateItem(item, empty);
        if(item == null || empty) {
            setText("");
        } else {
            setText(DateFormat.getTimeInstance(DateFormat.FULL).format(item));
        }
    }
}
