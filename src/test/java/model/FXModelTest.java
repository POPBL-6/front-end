package model;

import api.PSPort;
import data.MessagePublication;
import data.MessagePublish;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.ArrayUtils;

import java.io.IOException;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Created by Gorka Olalde on 19/5/16.
 */
public class FXModelTest {

    @TestSubject
    FXModel model;

    @Mock PSPort middlewareMock;

    @Before
    public void setUp() throws Exception {
        middlewareMock = createMock(PSPort.class);
        model = new FXModel();
    }

    @After
    public void tearDown() throws Exception {

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
    }

    @Test
    public void publish() throws Exception {
        String topicName = "topic";
        Topic topic = new Topic(topicName);
        Integer object = new Integer(1);
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
    public void publishNullObject() throws Exception {
        String topicName = "topic";
        Topic topic = new Topic(topicName);
        Integer object = null;
        Capture<MessagePublish> publicationCapture = newCapture();
        //record
        middlewareMock.addTopicListener(model);
        middlewareMock.publish(EasyMock.anyObject());
        replay(middlewareMock);
        //test
        model.initModel(middlewareMock);
        model.publish(topic, object);
        verify(middlewareMock);
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

}