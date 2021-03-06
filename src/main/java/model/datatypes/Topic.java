package model.datatypes;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Class where all the Topic information is stored in Properties.
 * @author Gorka Olalde
 */
public class Topic{

    private final SimpleStringProperty topicName;
    private final SimpleBooleanProperty subscribed;
    private final SimpleLongProperty lastValueTimestamp;
    private final SimpleObjectProperty<Object> lastValue;

    public Topic() {
        topicName = new SimpleStringProperty();
        subscribed = new SimpleBooleanProperty();
        lastValueTimestamp = new SimpleLongProperty();
        lastValue = new SimpleObjectProperty<>();
    }


    /**
     * Constructor to create a topic with it's name as paramenter.
     * @param name The name to be set.
     */
    public Topic(String name) {
        this();
        topicName.set(name);
    }


    /**
     * Constructor to create a topic based on it's name and the subscription status.
     * @param name
     * @param subscribed
     */
    public Topic(String name, boolean subscribed) {
        this(name);
        this.subscribed.set(subscribed);
    }

    public final SimpleStringProperty topicNameProperty() {
        return topicName;
    }

    public final SimpleBooleanProperty isSubscribedProperty() {
        return subscribed;
    }

    public final SimpleLongProperty lastValueTimestampProperty() {
        return lastValueTimestamp;
    }

    public final SimpleObjectProperty<Object> lastValueProperty() {
        return lastValue;
    }

    public String getTopicName() {
        return topicName.get();
    }

    public boolean isSubscribed() {
        return subscribed.get();
    }

    public long getlastTimestamp() {
        return lastValueTimestamp.get();
    }

    public Object getLastValue() {
        return lastValue.get();
    }


    public void setTopicName(String name) {
        topicName.set(name);
    }

    public void setSubscribed(boolean value) {
        subscribed.set(value);
    }

    public void setLastValueTimestamp(long timestamp) {
        lastValueTimestamp.set(timestamp);
    }

    public void setLastValue(Object value) {
        lastValue.set(value);
    }




}