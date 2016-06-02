package model.datatypes;

import javafx.beans.property.*;

/**
 * Created by Gorka Olalde on 18/5/16.
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



    public Topic(String name) {
        this();
        topicName.set(name);
    }

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