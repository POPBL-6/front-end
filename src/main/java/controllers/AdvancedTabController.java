package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.dialogs.CreateTopicDialog;
import controllers.dialogs.JFXDialogPane;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import model.AppMain;
import model.FXModel;
import model.datatypes.Topic;
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
    private JFXTextField searchField;

    @FXML
    private JFXButton subscribeBtn;

    @FXML
    private JFXButton unSubscribeBtn;

    @FXML
    private JFXButton deleteBtn;
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
        initSearchField();
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
        deleteBtn.setDisable(true);
        tableManager.hasItemsToSubscribeProperty().addListener((o, oldValue, newValue) -> {
            subscribeBtn.setDisable(!newValue);
        });
        tableManager.hasItemsToUnsubscribeProperty().addListener((o, oldValue, newValue) -> {
            unSubscribeBtn.setDisable(!newValue);
        });
        tableManager.selectedItemsProperty().addListener((ListChangeListener<Topic>) c -> {
           if (c.getList().size() > 0) {
               deleteBtn.setDisable(false);
           } else {
               deleteBtn.setDisable(true);
           }
        });
        subscribeBtn.setOnAction(e -> subscribeToSelection());
        unSubscribeBtn.setOnAction(e -> unSubscribeFromSelection());
        deleteBtn.setOnAction(e -> deleteSelectedTopics());
    }

    private void initSearchField() {
        searchField.textProperty().addListener((o, oldVal, newVal) -> tableManager.search(newVal));
    }

    /**
     * Subscribe to selection button action handler.
     */
    private void subscribeToSelection() {
        ArrayList<Topic> subscribeList = selectedItems.stream()
                .filter(topic -> !topic.isSubscribed())
                .collect(Collectors.toCollection(ArrayList::new));
        LOGGER.debug("Subscribed to " + subscribeList.size() + "topics");
        tableManager.clearTableSelection();
        if (subscribeList.size() > 0) {
            model.subscribe(subscribeList);
        }
    }

    /**
     * Unsubscribe from selection action handler.
     */
    private void unSubscribeFromSelection() {
        ArrayList<Topic> unSubscribeList = selectedItems.stream()
                .filter(topic -> topic.isSubscribed())
                .collect(Collectors.toCollection(ArrayList::new));
        tableManager.clearTableSelection();
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
    private void deleteSelectedTopics() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete topics?");
        alert.setContentText("Are you sure you wish to delete the selected topics?");
        alert.showAndWait().ifPresent(response -> {
            if(response == ButtonType.OK) {
                model.deleteTopics(selectedItems);
                tableManager.clearTableSelection();
            }
        });
    }
}
