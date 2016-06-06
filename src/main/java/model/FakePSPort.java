package model;

import api.PSPort;
import api.TopicListener;
import data.MessagePublication;
import data.MessagePublish;
import utils.ArrayUtils;

import java.io.IOException;

/**
 * Created by Gorka Olalde on 24/5/16.
 */
class FakePSPort implements PSPort{



    @Override
    public void disconnect() {

    }

    @Override
    public void subscribe(String... topics) {

    }

    @Override
    public void unsubscribe(String... topics) {

    }

    @Override
    public void publish(MessagePublish publication) {

    }

    @Override
    public MessagePublication getLastSample(String topic) {
        Integer data = 10;
        MessagePublication publication = null;
        try {
            publication = new MessagePublication("UTF-8", ArrayUtils.serialize(data), "topic1", "nisu", 1L);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return publication;
    }

    @Override
    public void addTopicListener(TopicListener listener) {

    }

    @Override
    public void removeTopicListener(TopicListener listener) {

    }

    @Override
    public String[] getAvailableTopics() {
        return new String[0];
    }
}
