package model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Gorka Olalde on 18/5/16.
 */
public class Topic {

    SimpleStringProperty topicName;
    SimpleBooleanProperty subscribed;
    SimpleLongProperty lastValueTimestamp;
    SimpleObjectProperty<Object> lastValue;

    public Topic() {
        topicName = new SimpleStringProperty();
        subscribed = new SimpleBooleanProperty();
        lastValueTimestamp = new SimpleLongProperty();
        lastValue = new SimpleObjectProperty<>();
    }

    public Topic(String name) {
        this();
        topicName.set(name);
    }

    public Topic(String name, boolean subscribed) {
        this(name);
        this.subscribed.set(subscribed);
    }
    public final SimpleStringProperty getTopicNameProperty() {
        return topicName;
    }

    public final SimpleBooleanProperty getIsSubscribedProperty() {
        return subscribed;
    }

    public final SimpleLongProperty getLastValueTimestampProperty() {
        return lastValueTimestamp;
    }

    public final SimpleObjectProperty<Object> getLastValueProperty() {
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