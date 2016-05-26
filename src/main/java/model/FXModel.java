package model;

import api.PSPort;
import api.TopicListener;
import data.MessagePublication;
import data.MessagePublish;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ArrayUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created by Gorka Olalde on 18/5/16.
 */
public class FXModel implements TopicListener {

    private static final Logger logger = LogManager.getLogger(FXModel.class);

    private final ObservableMap<String, Topic> topics = FXCollections.observableHashMap();

    private PSPort middleware;


    public void initModel(PSPort middleware) {
        this.middleware = middleware;
        middleware.addTopicListener(this);
    }


    public Topic getTopic(String topicName) {
        Topic retVal;
        retVal = topics.get(topicName);
        if (retVal == null) {
            retVal = new Topic(topicName);
        }
        return retVal;
    }

    public synchronized void subscribe(String... topicList) {
        Topic tempTopic;
        for (String topicName : topicList) {
            if (topics.containsKey(topicName)) {
                topics.get(topicName).setSubscribed(true);
            } else {
                tempTopic = new Topic(topicName, true);
                topics.put(topicName, tempTopic);
            }
        }
        middleware.subscribe(topicList);
    }

    public synchronized void subscribe(List<Topic> topicList) {
        ArrayList<String> topicNames = new ArrayList<>();
        String[] params;
        topicList.forEach(t -> {
            if (!topics.containsKey(t.getTopicName())) {
                topics.put(t.getTopicName(), t);
            }
            topicNames.add(t.getTopicName());
            t.setSubscribed(true);
        });
        params = new String[topicNames.size()];
        params = topicNames.toArray(params);
        middleware.subscribe(params);
    }

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


    public synchronized void unsubscribe(List<Topic> topicList) {
        ArrayList<String> topicNames = new ArrayList<>();
        topicList.forEach(t -> {
            if(topics.containsKey(t.getTopicName())) {
                topicNames.add(t.getTopicName());
                t.setSubscribed(false);
            }
        });
        middleware.unsubscribe((String[]) topicNames.toArray());
    }

    public synchronized void publish(Topic topic, Object value) {
        //TODO: Create changeListener for the values to publish them automatically when those are modified.
        MessagePublish message = new MessagePublish();
        topic.setLastValueTimestamp(System.currentTimeMillis());
        topic.setLastValue(value);
        message.setTopic(topic.getTopicName());
        try {
            message.setDataObject(value);
        } catch (IOException e) {
            logger.error("Error serializing object for topic " + topic.getTopicName());
        }
        middleware.publish(message);
        if(topics.get(topic) == null) {
            topics.put(topic.getTopicName(), topic);
        }
    }

    public synchronized void publish(Topic topic) {
        publish(topic, topic.getLastValue());
    }

    public ObservableMap<String, Topic> getTopics() {
        return topics;
    }


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

    public void forceRefresh(Topic topic) {
        forceRefresh(topic.getTopicName());
    }

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
}
