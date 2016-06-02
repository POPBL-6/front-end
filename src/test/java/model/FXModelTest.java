package model;

import api.PSPort;
import data.MessagePublication;
import data.MessagePublish;
import model.datatypes.Topic;
import org.easymock.Capture;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import utils.ArrayUtils;

import java.io.IOException;
import java.util.ArrayList;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Created by Gorka Olalde on 19/5/16.
 */
@RunWith(EasyMockRunner.class)
public class FXModelTest {

    @TestSubject
    private FXModel model;

    @Mock
    private PSPort middlewareMock;

    @Before
    public void setUp() throws Exception {
        middlewareMock = createMock(PSPort.class);
        model = new FXModel();
    }

    @Test
    public void getTopic() throws Exception {
        String topicName = "topic";
        Topic topic = new Topic(topicName);
        Topic returnTopic;

        model.getTopics().put(topicName, topic);
        returnTopic = model.getTopic(topicName);
        assertSame(topic, returnTopic);
    }

    @Test
    public void getTopicNew() throws Exception {
        String topicName = "newTopic";
        Topic returnTopic;
        returnTopic = model.getTopic(topicName);
        assertEquals(topicName, returnTopic.getTopicName());
    }

    @Test
    public void subscribeNew() throws Exception {
        String topicName = "topic1";
        Topic retrievedTopic;
        //record
        middlewareMock.addTopicListener(model);
        middlewareMock.subscribe(topicName);
        expectLastCall();
        replay(middlewareMock);
        //test
        model.initModel(middlewareMock);
        model.subscribe(topicName);
        verify(middlewareMock);
        retrievedTopic = model.getTopic(topicName);
        assertNotNull("Topic was not added to list", model.getTopic(topicName));
        assertEquals("Incorrect topic name", topicName, retrievedTopic.getTopicName());
    }

    @Test
    public void subscribeExisting() throws Exception {
        String topicName = "topic1";
        Topic existingTopic = new Topic(topicName);
        model.getTopics().put(topicName, existingTopic);
        //record
        middlewareMock.addTopicListener(model);
        middlewareMock.subscribe(topicName);
        expectLastCall();
        replay(middlewareMock);
        //test
        model.initModel(middlewareMock);
        model.subscribe(topicName);
        verify(middlewareMock);
        assertSame(existingTopic, model.getTopic(topicName));
    }

    @Test
    public void subscribeTopicList() {
        ArrayList<Topic> topics = new ArrayList();
        Topic testTopic = new Topic();
        Topic testTopic2 = new Topic();
        String topicName = "topic";
        String topicName2 = "topic2";
        testTopic.setTopicName(topicName);
        testTopic2.setTopicName(topicName2);
        topics.add(testTopic);
        topics.add(testTopic2);
        model.getTopics().put(testTopic.getTopicName(), testTopic);
        //record
        middlewareMock.addTopicListener(model);
        middlewareMock.subscribe(topicName, topicName2);
        expectLastCall();
        replay(middlewareMock);
        //test
        model.initModel(middlewareMock);
        model.subscribe(topics);
        verify();
        assertTrue(testTopic.isSubscribed());
        assertTrue(testTopic2.isSubscribed());
    }


    @Test
    public void unsubscribe() throws Exception {
        String topicName = "topic1";
        boolean subscribed = true;
        Topic existingTopic = new Topic(topicName, subscribed);
        Topic retrievedTopic;
        model.getTopics().put(topicName, existingTopic);
        //record
        middlewareMock.addTopicListener(model);
        middlewareMock.unsubscribe(topicName);
        expectLastCall();
        replay(middlewareMock);
        //test
        model.initModel(middlewareMock);
        model.unsubscribe(topicName);
        verify(middlewareMock);
        assertFalse("The topic was not set as unsubscribed", model.getTopic(topicName).isSubscribed());
    }

    @Test
    public void unsubscribeInvalid() throws Exception {
        String topicName = "invalidTopic";
        //record
        middlewareMock.addTopicListener(model);
        middlewareMock.unsubscribe(topicName);
        replay(middlewareMock);
        //test
        model.initModel(middlewareMock);
        model.unsubscribe(topicName);
        verify(middlewareMock);
        assertTrue(model.getTopics().size() == 0);
    }

    @Test
    public void unsubscribeList() throws Exception {
        ArrayList<Topic> topicList = new ArrayList<>();
        Topic topic1 = new Topic("topic1", true);
        Topic topic2 = new Topic("topic2", true);
        topicList.add(topic1);
        topicList.add(topic2);
        model.getTopics().put(topic1.getTopicName(), topic1);

        //record
        middlewareMock.addTopicListener(model);
        middlewareMock.unsubscribe(topic1.getTopicName());
        //test
        replay();
        model.initModel(middlewareMock);
        model.unsubscribe(topicList);
        verify();
        assertFalse(topic1.isSubscribed());
    }

    @Test
    public void publish() throws Exception {
        String topicName = "topic";
        Topic topic = new Topic(topicName);
        Integer object = 1;
        Capture<MessagePublish> publicationCapture = newCapture();
        //record
        middlewareMock.addTopicListener(model);
        middlewareMock.publish(capture(publicationCapture));
        replay(middlewareMock);
        //test
        model.initModel(middlewareMock);
        model.publish(topic, object);
        verify(middlewareMock);
        assertEquals("The sent object was incorrect"
                , publicationCapture.getValue().getDataObject()
                , object);
        assertEquals("Incorrect topic name was sent"
                , topicName
                , publicationCapture.getValue().getTopic());
    }

    @Test
    public void publishTopicObject() throws Exception {
        String topicName = "topic";
        Topic topic = new Topic(topicName);
        Integer object = 1;
        topic.setLastValue(object);
        Capture<MessagePublish> publicationCapture = newCapture();
        //record
        middlewareMock.addTopicListener(model);
        middlewareMock.publish(capture(publicationCapture));
        replay(middlewareMock);
        //test
        model.initModel(middlewareMock);
        model.publish(topic);
        verify(middlewareMock);
        assertEquals(object, publicationCapture.getValue().getDataObject());
    }

    @Test
    public void publishNonSerializableObject() throws Exception {
        String topicName = "topic";
        Topic topic = new Topic(topicName);
        topic.setLastValue(this);
        //record
        middlewareMock.addTopicListener(model);
        middlewareMock.publish(anyObject());
        replay(middlewareMock);
        //test
        model.initModel(middlewareMock);
        model.publish(topic, this);
        verify(middlewareMock);
        assertTrue(model.getTopics().size() == 1);
    }

    @Test
    public void forceRefresh() throws Exception {
        String topicName = "topic";
        Topic topic = new Topic(topicName);
        Integer dataObject = new Integer(10);
        long timestamp = 1l;
        MessagePublication mockedPublication = new MessagePublication("UTF-8"
                , ArrayUtils.serialize(dataObject)
                , topicName
                , "sender"
                , timestamp);
        model.getTopics().put(topicName, topic);
        //record
        middlewareMock.addTopicListener(model);
        expect(middlewareMock.getLastSample(topicName)).andReturn(mockedPublication);
        replay(middlewareMock);
        //test
        model.initModel(middlewareMock);
        model.forceRefresh(topicName);
        verify(middlewareMock);
        assertEquals("Stored value doesn't equal the received one"
                , dataObject
                , model.getTopic(topicName).getLastValue());
        assertEquals("Stored timestamp doesn't equal the received one"
                , timestamp
                , model.getTopic(topicName).getlastTimestamp());

    }

    @Test
    public void forceRefreshNonDeserializable() throws Exception {
        String topicName = "topic";
        Topic topic = new Topic(topicName);
        Integer dataObject = new Integer(10);
        long timestamp = 1l;
        //Store the topic with a valid last value
        topic.setLastValue(dataObject);
        model.getTopics().put(topicName, topic);
        //Create the message plublication and corrupt the stored object
        MessagePublication mockedPublication = new MessagePublication("UTF-8"
                , ArrayUtils.serialize(dataObject)
                , topicName
                , "sender"
                , timestamp);
        mockedPublication.getData()[0] = (byte)~mockedPublication.getData()[0];
        //record
        middlewareMock.addTopicListener(model);
        expect(middlewareMock.getLastSample(topicName)).andReturn(mockedPublication);
        replay(middlewareMock);
        //test
        model.initModel(middlewareMock);
        model.forceRefresh(topicName);
        verify(middlewareMock);
        assertSame(dataObject, topic.getLastValue());
        assertEquals("Stored timestamp doesn't equal the received one"
                , timestamp
                , model.getTopic(topicName).getlastTimestamp());

    }
    @Test
    public void forceRefreshTopicObject() throws IOException {
        Topic topic = new Topic("topic", true);
        model.getTopics().put(topic.getTopicName(), topic);
        int storedValue = 10;
        MessagePublication messagePublication = new MessagePublication(
                "UTF-8",
                ArrayUtils.serialize(storedValue),
                "topic",
                "sender1",
                1L
        );
        //record
        middlewareMock.addTopicListener(model);
        expect(middlewareMock.getLastSample(topic.getTopicName())).andReturn(messagePublication);
        replay(middlewareMock);
        //test
        model.initModel(middlewareMock);
        model.forceRefresh(topic);
        verify(middlewareMock);
        assertEquals(storedValue, model.getTopic("topic").getLastValue());

    }

    @Test
    public void publicationReceived() throws Exception {
        String topicName = "topic";
        boolean subscribed = true;
        Integer dataObject = new Integer(10);
        long timestamp = 1l;
        Topic topic = new Topic(topicName, subscribed);
        MessagePublication publication  = new MessagePublication("UTF-8"
                , ArrayUtils.serialize(dataObject)
                , topicName
                ,"sender"
                ,  timestamp);
        model.getTopics().put(topicName, topic);
        model.publicationReceived(publication);
        assertEquals("Received data doesn't match stored one"
                , dataObject
                , model.getTopic(topicName).getLastValue());
        assertEquals("Received timestamp doesn't match stored one"
                , timestamp
                , model.getTopic(topicName).getlastTimestamp());
    }

    @Test
    public void publicationReceivedNonDeserializable() throws Exception {
        String topicName = "topic";
        boolean subscribed = true;
        Integer dataObject = new Integer(10);
        long timestamp = 1l;
        //Store the topic in the model
        Topic topic = new Topic(topicName, subscribed);
        topic.setLastValue(dataObject);
        model.getTopics().put(topicName, topic);
        //Create the message publication message and corrupt the stored object
        MessagePublication publication  = new MessagePublication("UTF-8"
                , ArrayUtils.serialize(dataObject)
                , topicName
                ,"sender"
                ,  timestamp);
        publication.getData()[0] = (byte)~publication.getData()[0];
        model.publicationReceived(publication);
        assertEquals("Received data doesn't match stored one"
                , dataObject
                , model.getTopic(topicName).getLastValue());
        assertEquals("Received timestamp doesn't match stored one"
                , timestamp
                , model.getTopic(topicName).getlastTimestamp());
    }

    @Test
    public void publicationReceivedNotStoredTopic() throws Exception {
        String topicName = "topic";
        boolean subscribed = true;
        Integer dataObject = new Integer(10);
        long timestamp = 1l;
        //Create the message publication message and corrupt the stored object
        MessagePublication publication  = new MessagePublication("UTF-8"
                , ArrayUtils.serialize(dataObject)
                , topicName
                ,"sender"
                ,  timestamp);
        model.publicationReceived(publication);
        assertTrue("The topic was stored when we weren't expecting an upate for it",
                model.getTopics().isEmpty()
        );
    }
}