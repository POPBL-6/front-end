package controllers.tableUtils;

import javafx.scene.control.TableCell;
import model.Topic;

/**
 * Table Cell to format the subscribed status of a topic.
 * @author Gorka Olalde
 */
public class SubscribedTableCell extends TableCell<Topic, Boolean> {
    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText("");
        } else {
            if(item) {
                setText("Subscribed");
            } else {
                setText("Unsubscribed");
            }
        }
    }
}
