package model;

import api.PSPort;
import api.PSPortFactory;
import api.TopicListener;
import data.MessagePublication;
import data.MessagePublish;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ArrayUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gorka Olalde on 18/5/16.
 */
public class FXModel implements TopicListener {

    static final Logger logger = LogManager.getLogger(FXModel.class);

    Map<String, Topic> topics = new HashMap<>();

    PSPort middleware;


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

    public void subscribe(String... topicList) {
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

    public void unsubscribe(String... topicList) {
        Topic tempTopic;
        for (String topicName : topicList) {
            if (topics.containsKey(topicName)) {
                tempTopic = topics.get(topicName);
                tempTopic.setSubscribed(false);
            }
        }
        middleware.unsubscribe(topicList);
    }

    public void publish(Topic topic, Object value) {
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
    }

    public Map<String, Topic> getTopics() {
        return topics;
    }


    public void forceRefresh(String topicName) {
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
