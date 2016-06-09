package model;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Gorka Olalde on 8/6/16.
 */
public class DataUtilsIdentificationTests {


    @Test
    public void testStringObjectIdentification() {
        final String inputObject = "";
        final String expectedType = "String";
        String resultType = DataUtils.getStringDataType(inputObject);
        assertEquals(expectedType, resultType);
    }

    @Test
    public void testIntegerObjectIdentification() {
        final Integer inputObject = 10;
        final String expectedType = "Integer";
        String resultType = DataUtils.getStringDataType(inputObject);
        assertEquals(expectedType, resultType);
    }

    @Test
    public void testDoubleObjectIdentification() {
        final Double inputObject = 10.0;
        final String expectedType = "Double";
        String resultType = DataUtils.getStringDataType(inputObject);
        assertEquals(expectedType, resultType);
    }

    @Test
    public void testBooleanObjectIdentification() {
        final Boolean inputObject = true;
        final String expectedType = "Boolean";
        String resultType = DataUtils.getStringDataType(inputObject);
        assertEquals(expectedType, resultType);
    }

    @Test
    public void testUnknownObjectIdentification() {
        final Object inputObject = new Object();
        final String expectedType = "Unknown";
        String resultType = DataUtils.getStringDataType(inputObject);
        assertEquals(expectedType, resultType);
    }

    @Test
    public void testNullObjectIdentification() {
        final Object inputObject = null;
        final String expectedType = "Unknown";
        String resultType = DataUtils.getStringDataType(inputObject);
        assertEquals(expectedType, resultType);
    }


}
