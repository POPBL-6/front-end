package controllers.validators;

import com.jfoenix.controls.JFXTextField;
import controllers.tableutils.DataTypeTableCellTest;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.*;
import utils.JavaFXThreadingRule;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Gorka Olalde on 8/6/16.
 */
public class ValidTypeValidatorTests{

    ValidTypeValidator validator;


    JFXTextField textField;

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();


    @Before
    public void before() {
        validator = new ValidTypeValidator();
        textField = new JFXTextField();
        textField.setValidators(validator);
    }


    @Test
    public void validateInteger() {
        String validData = "10";
        String invalidData = "";
        validator.setDataType("Integer");
        textField.setText(validData);
        assertTrue(textField.validate());
        textField.setText(invalidData);
        assertFalse(textField.validate());
    }

    @Test
    public void validateDouble() {
        String validData = "10.5";
        String invalidData = "";
        validator.setDataType("Double");
        textField.setText(validData);
        assertTrue(textField.validate());
        textField.setText(invalidData);
        assertFalse(textField.validate());
    }

    @Test
    public void validateBoolean() {
        String validData = "true";
        String invalidData = "";
        validator.setDataType("Boolean");
        textField.setText(validData);
        assertTrue(textField.validate());
        textField.setText(invalidData);
        assertFalse(textField.validate());
    }

    @Test
    public void validateInet4Address() {
        String validData = "127.0.0.1";
        String invalidData = "300.300.300.0";
        validator.setDataType("InetAddress");
        textField.setText(validData);
        assertTrue(textField.validate());
        textField.setText(invalidData);
        assertFalse(textField.validate());
    }

    @Test
    public void validateInet6Address() {
        String validData = "fe80::";
        String invalidData = "gh80::";
        validator.setDataType("InetAddress");
        textField.setText(validData);
        assertTrue(textField.validate());
        textField.setText(invalidData);
        assertFalse(textField.validate());
    }

    @Test
    public void validateInetPort() {
        String validData = "80";
        String invalidData = "67000";
        validator.setDataType("InetPort");
        textField.setText(validData);
        assertTrue(textField.validate());
        textField.setText(invalidData);
        assertFalse(textField.validate());
    }

}
