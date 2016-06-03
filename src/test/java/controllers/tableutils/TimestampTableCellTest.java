package controllers.tableutils;

import controllers.AdvancedTabControllerTest;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.AfterClass;
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
    private static Thread jfxThread;

    @Override
    public void start(Stage primaryStage) throws Exception{

    }

    @BeforeClass
    public static void initJFX() {
        jfxThread = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(TimestampTableCellTest.class, new String[0]);
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
