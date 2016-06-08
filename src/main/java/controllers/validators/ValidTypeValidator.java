package controllers.validators;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextInputControl;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Gorka Olalde on 25/5/16.
 */
@DefaultProperty(value="icon")
public class ValidTypeValidator extends ValidatorBase {

    private String dataType = "String";

    @Override
    protected void eval() {
        if(srcControl.get() instanceof TextInputControl) {
            evalTextInputField();
        }
    }

    /**
     * Defines which dataType is used to validate if the input is correct or no.
     * @param dataType The dataType to be validated.
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }


    /**
     * Evaluate the input from a TextField. Evaluates the input of a textfield and depending on the dataType.
     */
    private void evalTextInputField(){
        TextInputControl textField = (TextInputControl) srcControl.get();
        String input = textField.getText();
        switch (dataType) {
            case "String":
                evalStringInput(input);
                break;
            case "Integer":
                evalNumberInput(input);
                break;
            case "Double":
                evalNumberInput(input);
                break;
            case "Boolean":
                evalBooleanInput(input);
                break;
            case "InetAddress":
                evalInetAddressInput(input);
                break;
            case "InetPort":
                evalInetPortInput(input);
                break;
            default:
                evalStringInput(input);
        }
    }


    /**
     * Verify if the input is a correct port.
     * @param input The input string value.
     */
    private void evalInetPortInput(String input) {
        int port;
        try {
            port = Integer.parseInt(input);
            if (port > 0 && port < 65534) {
                hasErrors.set(false);
            } else {
                hasErrors.set(true);
            }
        } catch (Exception e) {
            hasErrors.set(true);
        }
    }


    /**
     * Verify if the input is a correct IPv4 IPv6, or hostname.
     * @param input The input string value.
     */
    private void evalInetAddressInput(String input) {
        try {
            InetAddress.getByName(input);
            hasErrors.set(false);
        } catch (UnknownHostException e) {
            hasErrors.set(true);
        }
    }


    /**
     * Evaluates if the input data is a valid boolean value.
     * @param input boolean in String representation.
     */
    private void evalBooleanInput(String input) {
        String lowerInput = input.toLowerCase();
        if("true".equals(lowerInput) || "false".equals(lowerInput)) {
            hasErrors.set(false);
        } else {
            hasErrors.set(true);
        }
    }


    /**
     * Evaluates if the input data is a valid number.
     * @param input the number value (Integer or Double) to be validated.
     */
    private void evalNumberInput(String input) {
        try {
            Integer.parseInt(input);
            Double.parseDouble(input);
            hasErrors.set(false);
        } catch (Exception e) {
            hasErrors.set(true);
        }
    }


    /**
     * Evaluates if the input data is a correct and not empty String
     * @param input The string to be validated.
     */
    private void evalStringInput(String input) {
        if(!"".equals(input)) {
            hasErrors.set(false);
        } else {
            hasErrors.set(true);
        }
    }


}
