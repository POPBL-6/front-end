package controllers.tableutils;

import controllers.AdvancedTabControllerTest;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.*;
import utils.JavaFXThreadingRule;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Gorka Olalde on 30/5/16.
 */
public class SubscribedTableCellTest {

    private SubscribedTableCell tableCell;

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();


    @Before
    public void before() {
        tableCell = new SubscribedTableCell();
    }

    @Test
    public void unsubscribedTest() {
        boolean subscribed = false;
        boolean empty = false;
        tableCell.updateItem(subscribed, empty);
        assertEquals("Unsubscribed", tableCell.getText());
    }

    @Test
    public void subscribedTest() {
        boolean subscribed = true;
        boolean empty = false;
        tableCell.updateItem(subscribed, empty);
        assertEquals("Subscribed", tableCell.getText());
    }

    @Test
    public void emptyTest() {
        boolean empty = true;
        tableCell.updateItem(null, empty);
        assertEquals("", tableCell.getText());
    }

    @Test
    public void nullTest() {
        boolean empty = false;
        tableCell.updateItem(null, empty);
        assertEquals("", tableCell.getText());
    }

    @Test
    public void validButEmptyTest() {
        boolean value = true;
        boolean empty = true;
        tableCell.updateItem(value, empty);
        assertEquals("", tableCell.getText());
    }

}
