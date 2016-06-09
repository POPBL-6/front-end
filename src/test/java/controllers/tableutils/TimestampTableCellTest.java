package controllers.tableutils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import utils.JavaFXThreadingRule;

import java.text.DateFormat;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Gorka Olalde on 30/5/16.
 */
public class TimestampTableCellTest{

    private TimestampTableCell tableCell;

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();


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
