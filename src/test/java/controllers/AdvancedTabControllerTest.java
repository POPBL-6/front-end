package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.FXModel;
import model.datatypes.Topic;
import org.easymock.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.easymock.EasyMock.*;

/**
 * Created by Gorka Olalde on 30/5/16.
 */
@RunWith(EasyMockRunner.class)
public class AdvancedTabControllerTest extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{

    }

    @BeforeClass
    public static void initJFX() {
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(AdvancedTabControllerTest.class, new String[0]);
            }
        };
        t.setDaemon(true);
        t.start();
    }

    @TestSubject
    AdvancedTabController controller;

    @Mock
    FXModel model;

    @Mock
    TopicTableManager manager;
    @Before
    public void before() throws NoSuchFieldException, IllegalAccessException {
        model = EasyMock.createMock(FXModel.class);
        manager = EasyMock.createMock(TopicTableManager.class);
        controller = new AdvancedTabController();
        Field modelField = AdvancedTabController.class.getDeclaredField("model");
        Field tableManagerField = AdvancedTabController.class.getDeclaredField("tableManager");
        modelField.setAccessible(true);
        tableManagerField.setAccessible(true);
        modelField.set(controller, model);
        tableManagerField.set(controller, manager);
    }


    @Test
    public void testInitButtons() throws Exception {
        Field subscribeBtnField = AdvancedTabController.class.getDeclaredField("subscribeBtn");
        Field unsubscribeBtnField = AdvancedTabController.class.getDeclaredField("unSubscribeBtn");
        Field deleteBtnField = AdvancedTabController.class.getDeclaredField("deleteBtn");
        Method initButtonsMethod = AdvancedTabController.class.getDeclaredMethod("initButtons");
        subscribeBtnField.setAccessible(true);
        unsubscribeBtnField.setAccessible(true);
        deleteBtnField.setAccessible(true);
        initButtonsMethod.setAccessible(true);

        JFXButton subscribeBtn = new JFXButton();
        JFXButton unSubscribeBtn = new JFXButton();
        JFXButton deleteBtn = new JFXButton();
        SimpleBooleanProperty itemsToSubscribe = new SimpleBooleanProperty();
        SimpleBooleanProperty itemsToUnSubscribe = new SimpleBooleanProperty();
        ObservableList<Topic> selectedItems = FXCollections.observableArrayList();
        itemsToSubscribe.set(false);
        itemsToUnSubscribe.set(false);
        subscribeBtnField.set(controller, subscribeBtn);
        unsubscribeBtnField.set(controller, unSubscribeBtn);
        deleteBtnField.set(controller, deleteBtn);
        //record
        expect(manager.hasItemsToSubscribeProperty()).andReturn(itemsToSubscribe);
        expect(manager.hasItemsToUnsubscribeProperty()).andReturn(itemsToUnSubscribe);
        expect(manager.selectedItemsProperty()).andReturn(selectedItems);
        replay(manager);
        //play
        initButtonsMethod.invoke(controller);
        //verify
        verify(manager);
        assertTrue(subscribeBtn.isDisabled());
        assertTrue(unSubscribeBtn.isDisabled());
        assertTrue(deleteBtn.isDisabled());
        itemsToSubscribe.set(true);
        assertFalse(subscribeBtn.isDisabled());
        itemsToUnSubscribe.set(true);
        assertFalse(unSubscribeBtn.isDisabled());
        selectedItems.add(new Topic());
        assertFalse(deleteBtn.isDisabled());
        selectedItems.clear();
        assertTrue(deleteBtn.isDisabled());
    }

    @Test
    public void subscribeToSelection() throws Exception {
        //Get Fields and methods
        Field selectedItemsField = AdvancedTabController.class.getDeclaredField("selectedItems");
        selectedItemsField.setAccessible(true);
        Method subscribeToSelectionMethod = AdvancedTabController.class.getDeclaredMethod("subscribeToSelection");
        subscribeToSelectionMethod.setAccessible(true);
        //Set list and init capture
        ObservableList<Topic> selectedItems = FXCollections.observableArrayList();
        Capture<ArrayList<Topic>> paramCapture = newCapture();
        selectedItemsField.set(controller, selectedItems);
        Topic topic = new Topic("topic");
        topic.setSubscribed(false);
        selectedItems.add(topic);
        //record
        model.subscribe(capture(paramCapture));
        replay(model);
        //play
        subscribeToSelectionMethod.invoke(controller);
        verify(model);
        assertTrue(paramCapture.getValue().size() == 1);
    }

    @Test
    public void subscribeToSelectionAlreadySubscribed() throws Exception {
        //Get Fields and methods
        Field selectedItemsField = AdvancedTabController.class.getDeclaredField("selectedItems");
        selectedItemsField.setAccessible(true);
        Method subscribeToSelectionMethod = AdvancedTabController.class.getDeclaredMethod("subscribeToSelection");
        subscribeToSelectionMethod.setAccessible(true);
        //Set list and init capture
        ObservableList<Topic> selectedItems = FXCollections.observableArrayList();
        selectedItemsField.set(controller, selectedItems);
        Topic topic = new Topic("topic");
        topic.setSubscribed(true);
        selectedItems.add(topic);
        //record
        replay(model);
        //play
        subscribeToSelectionMethod.invoke(controller);
        verify(model);
    }

    @Test
    public void subscribeToSelectionNoItems() throws Exception {
        //Get Fields and methods
        Field selectedItemsField = AdvancedTabController.class.getDeclaredField("selectedItems");
        selectedItemsField.setAccessible(true);
        Method subscribeToSelectionMethod = AdvancedTabController.class.getDeclaredMethod("subscribeToSelection");
        subscribeToSelectionMethod.setAccessible(true);
        //Set list and init capture
        ObservableList<Topic> selectedItems = FXCollections.observableArrayList();
        selectedItemsField.set(controller, selectedItems);
        //record
        replay(model);
        //play
        subscribeToSelectionMethod.invoke(controller);
        verify(model);
    }

    @Test
    public void unsubscribeFromSelection() throws Exception {
        //Get Fields and methods
        Field selectedItemsField = AdvancedTabController.class.getDeclaredField("selectedItems");
        selectedItemsField.setAccessible(true);
        Method unsubscribeFromSelectionMethod = AdvancedTabController.class.getDeclaredMethod("unSubscribeFromSelection");
        unsubscribeFromSelectionMethod.setAccessible(true);
        //Set list and init capture
        ObservableList<Topic> selectedItems = FXCollections.observableArrayList();
        Capture<ArrayList<Topic>> paramCapture = newCapture();
        selectedItemsField.set(controller, selectedItems);
        Topic topic = new Topic("topic");
        topic.setSubscribed(true);
        selectedItems.add(topic);
        //record
        model.unsubscribe(capture(paramCapture));
        replay(model);
        //play
        unsubscribeFromSelectionMethod.invoke(controller);
        verify(model);
        assertTrue(paramCapture.getValue().size() == 1);
    }

    @Test
    public void unsubscribeFromSelectionAlreadyUnSubscribed() throws Exception {
        //Get Fields and methods
        Field selectedItemsField = AdvancedTabController.class.getDeclaredField("selectedItems");
        selectedItemsField.setAccessible(true);
        Method unsubscribeFromSelectionMethod = AdvancedTabController.class.getDeclaredMethod("unSubscribeFromSelection");
        unsubscribeFromSelectionMethod.setAccessible(true);
        //Set list and init capture
        ObservableList<Topic> selectedItems = FXCollections.observableArrayList();
        selectedItemsField.set(controller, selectedItems);
        Topic topic = new Topic("topic");
        topic.setSubscribed(false);
        selectedItems.add(topic);
        //record
        replay(model);
        //play
        unsubscribeFromSelectionMethod.invoke(controller);
        verify(model);
    }

    @Test
    public void unsubscribeFromSelectionNoItems() throws Exception {
        //Get Fields and methods
        Field selectedItemsField = AdvancedTabController.class.getDeclaredField("selectedItems");
        selectedItemsField.setAccessible(true);
        Method unsubscribeFromSelectionMethod = AdvancedTabController.class.getDeclaredMethod("unSubscribeFromSelection");
        unsubscribeFromSelectionMethod.setAccessible(true);
        //Set list and init capture
        ObservableList<Topic> selectedItems = FXCollections.observableArrayList();
        selectedItemsField.set(controller, selectedItems);
        //record
        replay(model);
        //play
        unsubscribeFromSelectionMethod.invoke(controller);
        verify(model);
    }
}
