package model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.fail;

/**
 * Created by Gorka Olalde on 8/6/16.
 */
@RunWith(Parameterized.class)
public class DataUtilsIncorrectNumbersTest {

    @Parameterized.Parameter
    public String dataType;
    @Parameterized.Parameter(value = 1)
    public String input;


    @Parameterized.Parameters
    public static Collection inputs() {
        return Arrays.asList(new Object[][]{{"Integer", "Test"},
                {"Integer", ""},
                {"Double", "Test"},
                {"Double", ""}
        });
    }

    @Test(expected = NumberFormatException.class)
    public void testIncorrectNumbers() {
        DataUtils.createObjectByType(dataType, input);
        fail("Exception not thrown");
    }
}
