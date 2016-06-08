package model;

import api.PSPort;
import api.TopicListener;
import data.MessagePublication;
import data.MessagePublish;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.datatypes.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ArrayUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Model where all the data operations will be done an from which all the controllers will obtain the data.
 * @author Gorka Olalde
 */
public class FXModel implements TopicListener {

    private static final Logger logger = LogManager.getLogger(FXModel.class);

    private final ObservableMap<String, Topic> topics = FXCollections.observableHashMap();

    private PSPort middleware;

    /**
     * Initializes the model by setting the middleware instance and setting itself as a listener of the middleware.
     * @param middleware
     */
    public void initModel(PSPort middleware) {
        this.middleware = middleware;
        middleware.addTopicListener(this);
    }

    /**
     * Retrieve a topic based on it's name.
     * @param topicName The name of the topic.
     * @return The retrieved topic. Null if the topic is not found.
     */
    public Topic getTopic(String topicName) {
        Topic retVal;
        retVal = topics.get(topicName);
        if (retVal == null) {
            retVal = new Topic(topicName);
        }
        return retVal;
    }

    /**
     * Subscribe to a single topic by using the topic object as paramenter.
     * @param topic The topic to subscribe to.
     */
    public synchronized void subscribe(Topic topic) {
        if (!topics.containsKey(topic.getTopicName())) {
            topics.put(topic.getTopicName(), topic);
        }
        topic.setSubscribed(true);
        middleware.subscribe(topic.getTopicName());
    }

    /**
     * Subscribe to a topic list based on a array of Strings with the topic names.
     * @param topicList The topic list stored in a String array.
     */
    public synchronized void subscribe(String... topicList) {
        Topic tempTopic;
        for (String topicName : topicList) {
            if (topics.containsKey(topicName)) {
                topics.get(topicName).setSubscribed(true);
            } else {
                tempTopic = new Topic(topicName, true);
                tempTopic.setSubscribed(true);
                topics.put(topicName, tempTopic);
            }
        }
        middleware.subscribe(topicList);
    }

    /**
     * Subscribe to a list of topics based on a List instance storing all the Topic objects.
     * @param topicList The list containing all the topics.
     */
    public synchronized void subscribe(List<Topic> topicList) {
        ArrayList<String> topicNames = new ArrayList<>();
        String[] params;
        topicList.forEach(t -> {
            if (!topics.containsKey(t.getTopicName())) {
                t.setSubscribed(true);
                topics.put(t.getTopicName(), t);
            }
            topicNames.add(t.getTopicName());
            t.setSubscribed(true);
        });
        params = new String[topicNames.size()];
        params = topicNames.toArray(params);
        middleware.subscribe(params);
    }

    /**
     * Unsubscribe to a topic list based on a array of Strings with the topic names.
     * @param topicList The topic list stored in a String array.
     */
    public synchronized void unsubscribe(String... topicList) {
        Topic tempTopic;
        for (String topicName : topicList) {
            if (topics.containsKey(topicName)) {
                tempTopic = topics.get(topicName);
                tempTopic.setSubscribed(false);
            }
        }
        middleware.unsubscribe(topicList);
    }

    /**
     * Unsubscribe to a list of topics based on a List instance storing all the Topic objects.
     * @param topicList The list containing all the topics.
     */
    public synchronized void unsubscribe(List<Topic> topicList) {
        ArrayList<String> topicNames = new ArrayList<>();
        String[] params;
        topicList.forEach(t -> {
            if(topics.containsKey(t.getTopicName())) {
                topicNames.add(t.getTopicName());
                t.setSubscribed(false);
            }
        });
        params = new String[topicNames.size()];
        params = topicNames.toArray(params);
        middleware.unsubscribe(params);
    }
    /**
     * Unsubscribe from a single topic by using the topic object as paramenter.
     * @param topic The topic to unsubscribe from.
     */
    public synchronized void unsubscribe(Topic topic) {
        if (topic.isSubscribed()) {
            middleware.unsubscribe(topic.getTopicName());
        }
    }

    /**
     * Publish a new value by using the topic object and the object to publish as paramenter.
     * @param topic The topic object.
     * @param value The object to publish.
     */
    public synchronized void publish(Topic topic, Object value) {
        //TODO: Create changeListener for the values to publish them automatically when those are modified.
        MessagePublish message;
        try {
            message = new MessagePublish(topic.getTopicName(), topic.getLastValue());
            message.setDataObject(value);
            middleware.publish(message);
        } catch (IOException e) {
            logger.error("Error serializing object for topic " + topic.getTopicName());
        }
        if(topics.get(topic) == null) {
            topics.put(topic.getTopicName(), topic);
        }
    }

    /**
     * Publish a new value by using a Topic object and by getting the object to publish from it.
     * @param topic The topic to publish
     */
    public synchronized void publish(Topic topic) {
        publish(topic, topic.getLastValue());
    }

    /**
     * Returns the ObservableMap containing all the topics.
     * @return The Map storing the topics.
     */
    public ObservableMap<String, Topic> getTopics() {
        return topics;
    }

    /**
     * Forces the refresh of a stored topic value by using it's topic name as parameter.
     * @param topicName The topic to refresh.
     */
    public synchronized void forceRefresh(String topicName) {
        Topic topic = topics.get(topicName);
        MessagePublication returnMessage = middleware.getLastSample(topicName);
        if (topic != null && returnMessage != null) {
            topic.setLastValueTimestamp(returnMessage.getTimestamp());
            try {

                topic.setLastValue(ArrayUtils.deserialize(returnMessage.getData()));
            } catch (IOException e) {
                logger.error("IOException deserializing object || topic: "
                        + returnMessage.getTopic() + " || sender: " + returnMessage.getSender(), e);
            } catch (ClassNotFoundException e) {
                logger.error("Error deserializing object , class was not found || topic: "
                        + returnMessage.getTopic() + " || sender: " + returnMessage.getSender());
            }
        }
    }

    /**
     * Refresh a stored topic value by using the topic object as parameter.
     * @param topic The topic object to refresh.
     */
    public void forceRefresh(Topic topic) {
        forceRefresh(topic.getTopicName());
    }


    /**
     * Method to update the topic object when a new value has been received from the middleware.
     * @param message The received Message.
     */
    @Override
    public void publicationReceived(MessagePublication message) {
        Topic topic;
        topic = topics.get(message.getTopic());

        if (topic != null) {
            topic.setTopicName(message.getTopic());
            topic.setLastValueTimestamp(message.getTimestamp());
            try {
                topic.setLastValue(message.getDataObject());
            } catch (IOException e) {
                logger.error("IOException deserializing object || topic: "
                        + message.getTopic() + " || sender: " + message.getSender(), e);
            } catch (ClassNotFoundException e) {
                logger.error("Error deserializing object , class was not found || topic: "
                        + message.getTopic() + " || sender: " + message.getSender());
            }
        }
    }

    /**
     * Deletes a list of topics from the topic list.
     * @param topics The topics to be deleted stored in a List.
     */
    public void deleteTopics(List<Topic> topics) {
        for(Topic topic : topics) {
            if(topic.isSubscribed()) {
                this.unsubscribe(topic);
            }
            this.topics.remove(topic.getTopicName());
        }
    }


}
