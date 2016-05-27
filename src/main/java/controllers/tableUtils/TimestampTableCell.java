package controllers.tableUtils;

import javafx.scene.control.TableCell;
import model.Topic;

import java.text.DateFormat;

/**
 * TableCell to format the timestamps of the Topic.
 * @author Gorka Olalde
 */
public class TimestampTableCell extends TableCell<Topic, Long>{
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
