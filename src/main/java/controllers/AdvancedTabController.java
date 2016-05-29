package controllers;

import com.jfoenix.controls.JFXButton;
import controllers.dialogs.CreateTopicDialog;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import model.AppMain;
import model.FXModel;
import model.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by Gorka Olalde on 19/5/16.
 */


public class AdvancedTabController {

    private static final Logger LOGGER = LogManager.getLogger(AdvancedTabController.class);

    private FXModel model;

    @FXML
    private JFXButton subscribeBtn;

    @FXML
    private JFXButton unSubscribeBtn;

    @FXML
    private TableView<Topic> topicTable;

    private TopicTableManager tableManager;

    private ObservableList<Topic> selectedItems;

    /**
     * Sets the model to be used in the controller.
     * @param model The FXModel instance
     */
    private void setModel(FXModel model) {
        this.model = model;
    }

    /**
     * Initializes the controller with the default settings and assuming that a controller has been already set.
     */
    private void initController() {
        initTopicTableManager();
        initButtons();
        LOGGER.info("AdvancedTabController initialization finished.");
    }

    /**
     * Initializes the controller by setting it's model first.
     * @param model the FXModel instance.
     */
    public void initController(FXModel model) {
        setModel(model);
        initController();
    }


    private void initTopicTableManager() {
        tableManager = new TopicTableManager(topicTable);
        tableManager.initialize(model.getTopics());
        selectedItems = tableManager.selectedItemsProperty();
    }

    private void initButtons() {
        subscribeBtn.setDisable(true);
        unSubscribeBtn.setDisable(true);
        tableManager.hasItemsToSubscribeProperty().addListener((o, oldValue, newValue) -> {
            subscribeBtn.setDisable(!newValue);
        });
        tableManager.hasItemsToUnsubscribeProperty().addListener((o, oldValue, newValue) -> {
            unSubscribeBtn.setDisable(!newValue);
        });
    }


    /**
     * Subscribe to selection button action handler.
     */
    @FXML
    private void subscribeToSelection() {
        ArrayList<Topic> subscribeList = selectedItems.stream()
                .filter(topic -> !topic.isSubscribed())
                .collect(Collectors.toCollection(ArrayList::new));
        LOGGER.debug("Subscribed to " + subscribeList.size() + "topics");
        if (subscribeList.size() > 0) {
            model.subscribe(subscribeList);
        }
    }

    /**
     * Unsubscribe from selection action handler.
     */
    @FXML
    private void unSubscribeFromSelection() {
        ArrayList<Topic> unSubscribeList = selectedItems.stream()
                .filter(topic -> topic.isSubscribed())
                .collect(Collectors.toCollection(ArrayList::new));
        LOGGER.debug("Unsubscribed from " + unSubscribeList.size() + " topics");
        if (unSubscribeList.size() > 0) {
            model.unsubscribe(unSubscribeList);
        }
    }

    /**
     * Create new topic button handler.
     * @throws IOException
     */
    @FXML
    private void createNewTopic() {
        CreateTopicDialog dialog = new CreateTopicDialog(AppMain.getStage());
        dialog.showAndWait().ifPresent(response -> {
            if(response !=null) {
                LOGGER.debug("Dialog response topic received.");
                model.publish(response);
            }
        });

    }

    /**
     * Delete selected topics button handler.
     */
    @FXML
    private void deleteSelectedTopics() {
        //TODO:Add confirm dialog and action to delete topics.
    }
}
