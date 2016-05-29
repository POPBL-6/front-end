package controllers.tableutils;

import controllers.AdvancedTabControllerTest;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.Socket;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.failSame;

/**
 * Created by Gorka Olalde on 30/5/16.
 */
public class DataTypeTableCellTest extends Application{

    DataTypeTableCell tableCell;

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
        tableCell = new DataTypeTableCell();
    }

    @Test
    public void testBoolean() {
        boolean testBool = true;
        boolean empty = false;
        tableCell.updateItem(testBool, empty);
        assertEquals("Boolean", tableCell.getText());
    }

    @Test
    public void testInteger() {
        int testInt = 10;
        boolean empty = false;
        tableCell.updateItem(testInt, empty);
        assertEquals("Integer", tableCell.getText());
    }

    @Test
    public void testDouble() {
        double testDouble = 10.2;
        boolean empty = false;
        tableCell.updateItem(testDouble, empty);
        assertEquals("Double", tableCell.getText());
    }

    @Test
    public void testString() {
        String testString = "test";
        boolean empty = false;
        tableCell.updateItem(testString, empty);
        assertEquals("String", tableCell.getText());
    }

    @Test
    public void testEmpty() {
        boolean empty = true;
        tableCell.updateItem(null, empty);
        assertEquals("", tableCell.getText());
    }

    @Test
    public void testNull() {
        boolean empty = false;
        tableCell.updateItem(null, empty);
        assertEquals("", tableCell.getText());
    }

    @Test
    public void testUnknown() {
        Object unknownObject = new Socket();
        boolean empty = false;
        tableCell.updateItem(unknownObject, empty);
        assertEquals("Unknown class", tableCell.getText());
    }
}
