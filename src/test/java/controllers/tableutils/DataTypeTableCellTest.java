package controllers.tableutils;

import controllers.AdvancedTabControllerTest;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.*;
import utils.JavaFXThreadingRule;

import java.net.Socket;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Gorka Olalde on 30/5/16.
 */


public class DataTypeTableCellTest{

    private DataTypeTableCell tableCell;

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();


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
    public void testValidButEmpty() {
        int value = 10;
        boolean empty = true;
        tableCell.updateItem(value, empty);
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
