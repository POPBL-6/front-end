package model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Gorka Olalde on 8/6/16.
 */
public class DataUtilsCreationTests {

    @Test
    public void stringCreationTest() {
        final String dataType = "String";
        final String input = "test";
        Object result = DataUtils.createObjectByType(dataType, input);
        assertTrue(result instanceof String);
    }

    @Test
    public void booleanCreationTest() {
        final String dataType = "Boolean";
        final String input = "true";
        final boolean expected = true;
        Object result = DataUtils.createObjectByType(dataType, input);
        assertTrue(result instanceof Boolean);
        assertEquals(expected, result);
    }

    @Test
    public void integerCreatioonTest() {
        final String dataType = "Integer";
        final String input = "10";
        final int expected = 10;
        Object result = DataUtils.createObjectByType(dataType, input);
        assertTrue(result instanceof Integer);
        assertEquals(expected, result);
    }

    @Test
    public void doubleCreationTest() {
        final String dataType = "Double";
        final String input = "10.0";
        final Double expected = 10.0;
        Object result = DataUtils.createObjectByType(dataType, input);
        assertTrue(result instanceof Double);
        assertEquals(expected, result);
    }

    @Test
    public void unknownCreationTest() {
        final String dataType = "Unknown";
        final String input = "test";
        final String expected = "test";
        Object result = DataUtils.createObjectByType(dataType, input);
        assertTrue(result instanceof String);
        assertEquals(expected, result);
    }


}
