package controllers.tableutils;

import controllers.AdvancedTabControllerTest;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Gorka Olalde on 30/5/16.
 */
public class SubscribedTableCellTest extends Application {

    private static Thread jfxThread;
    private SubscribedTableCell tableCell;

    @Override
    public void start(Stage primaryStage) throws Exception{

    }

    @BeforeClass
    public static void initJFX() {
        jfxThread = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(SubscribedTableCellTest.class, new String[0]);
            }
        };
        jfxThread.setDaemon(true);
        jfxThread.start();
    }

    @AfterClass
    public static void stopJFX() {
        try {
            jfxThread.interrupt();
            jfxThread.join(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

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
