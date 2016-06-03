package controllers.tableutils;

import controllers.AdvancedTabControllerTest;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.DateFormat;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Gorka Olalde on 30/5/16.
 */
public class TimestampTableCellTest extends Application{

    private TimestampTableCell tableCell;

    @Override
    public void start(Stage primaryStage) throws Exception{

    }

    @BeforeClass
    public static void initJFX() {
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(AdvancedTabControllerTest.class, new String[0]);
            }
        };
        t.setDaemon(true);
        t.start();
    }

    @Before
    public void before() {
        tableCell = new TimestampTableCell();
    }


    @Test
    public void testCorrectTime() {
        long currTime = System.currentTimeMillis();
        String expected = DateFormat.getTimeInstance(DateFormat.FULL).format(currTime);
        boolean empty = false;
        tableCell.updateItem(currTime, empty);
        assertEquals(expected, tableCell.getText());
    }

    @Test
    public void emptyField() {
        boolean empty = true;
        tableCell.updateItem(null, empty);
        assertEquals("", tableCell.getText());
    }

    @Test
    public void nullObject() {
        boolean empty = false;
        tableCell.updateItem(null, empty);
        assertEquals("", tableCell.getText());
    }

    @Test
    public void emptyNotNull() {
        boolean empty = true;
        long timestamp = System.currentTimeMillis();
        tableCell.updateItem(timestamp, empty);
        assertEquals("", tableCell.getText());
    }

}
