package model;

import model.datatypes.Topic;

/**
 * Created by Gorka Olalde on 7/6/16.
 */
public class DataUtils {

    public static Object createObjectByType(String dataType, String input) {
        Object outputObject;
        switch (dataType) {
            case "String":
                outputObject = input;
                break;
            case "Integer":
                outputObject = Integer.parseInt(input);
                break;
            case "Double":
                outputObject = Double.parseDouble(input);
                break;
            case "Boolean":
                outputObject = Boolean.parseBoolean(input);
                break;
            default:
                outputObject = input;
                break;
        }
        return outputObject;
    }

    public static String getStringDataType(Object dataSample) {
        if (dataSample instanceof String) {
            return "String";
        } else if (dataSample instanceof Double) {
            return "Double";
        } else if (dataSample instanceof Integer) {
            return "Integer";
        } else if (dataSample instanceof Boolean) {
            return "Boolean";
        } else {
            return "Unknown";
        }
    }
}
