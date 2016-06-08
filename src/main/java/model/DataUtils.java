package model;

/**
 * Class for presenting basic utilities to get
 * the class of an object in string representation or creater an object from it.
 * @author Gorka Olalde
 */
public class DataUtils {

    /**
     * Creates a new object of type x depending on the dataType.
     * Returns a new object depending of which datatype has been entered and the input value.
     * @param dataType The type of the data to return.
     * @param input The input data to be parsed.
     * @return The object containing the data.
     */
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

    /**
     * Get the simplified object class as an string.
     * @param dataSample The data to get the type from.
     * @return The class in simplified string representation.
     */
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
