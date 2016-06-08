package controllers.tableutils;

import javafx.scene.control.TableCell;
import model.datatypes.Topic;

/**
 * Table Cell to format the subscribed status of a topic.
 * @author Gorka Olalde
 */
public class SubscribedTableCell extends TableCell<Topic, Boolean> {

    /**
     * Updates the table cell to show if the application is subscribed to the topic or not.
     * @param item The boolean that shows if the we are subscribed to the topic or not.
     * @param empty If the cell is empty.
     */
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
