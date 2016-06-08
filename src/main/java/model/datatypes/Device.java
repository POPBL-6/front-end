package model.datatypes;

/**
 * Simple POJO to store the device data in the FXML files and retrieve it later.
 * @author Gorka Olalde
 */
public class Device {
    private String topicName;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
