package controllers;

import com.jfoenix.controls.*;
import controllers.dialogs.CreateTopicDialog;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
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

    private static final Logger logger = LogManager.getLogger(AdvancedTabController.class);

    private FXModel model;

    private final ObservableList topicsList = FXCollections.observableArrayList();
    private ObservableList<Topic> selectedItems = FXCollections.observableArrayList();


    @FXML
    private JFXButton subscribeBtn;

    @FXML
    private JFXButton unSubscribeBtn;

    @FXML
    private TableView<Topic> topicTable;

    @FXML
    private TableColumn<Topic, String> topicNameColumn;

    @FXML
    private TableColumn<Topic, Boolean> subscribedColumn;

    @FXML
    private TableColumn<Topic, Long> timestampColumn;

    @FXML
    private TableColumn<Topic, Object> dataTypeColumn;

    @FXML
    private TableColumn<Topic, Object> valueColumn;


    /**
     * Sets the model to be used in the controller.
     * @param model The FXModel instance
     */
    protected void setModel(FXModel model) {
        this.model = model;
    }

    /**
     * Initializes the controller with the default settings and assuming that a controller has been already set.
     */
    protected void initController() {
        initTableList();
        initTable();
        initColumns();
        initCellRenderers();
    }

    /**
     * Initializes the controller by setting it's model first.
     * @param model the FXModel instance.
     */
    protected void initController(FXModel model) {
        setModel(model);
        initController();
    }

    /**
     * Initializes the table in which the topics are shown.
     */
    private void initTable() {
        initTableList();
        topicTable.itemsProperty().set(topicsList);
        selectedItems = topicTable.getSelectionModel().getSelectedItems();
        createSubscriptionButtonEnabler();
    }

    /**
     * Initializes the columns of the topic table and binds them with the properties inside the Topic class.
     * @see Topic
     */
    private void initColumns() {
        topicNameColumn.setCellValueFactory(new PropertyValueFactory<>("topicName"));
        subscribedColumn.setCellValueFactory(new PropertyValueFactory<>("isSubscribed"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        dataTypeColumn.setCellValueFactory(new PropertyValueFactory<>("lastValue"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("lastValue"));
    }

    /**
     * Initializes the cell renderers of the table.
     */
    private void initCellRenderers() {
        dataTypeColumn.setCellFactory(column -> new TableCell<Topic, Object>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null || empty) {
                    setText("Unknown");
                } else {
                    if (item instanceof String) {
                        setText("String");
                    } else if (item instanceof Integer) {
                        setText("Integer");
                    } else if (item instanceof Boolean) {
                        setText("Boolean");
                    } else if (item instanceof Double) {
                        setText("Double");
                    } else {
                        setText("Unknown class");
                    }
                }
            }});
    }

    /**
     * Create listener for enabling or disabling the subscribe and unsubscribe buttons depending on the selection.
     * Depending of the current subscribe state of the selected topics in the table, enables or disables the buttons.
     */
    private void createSubscriptionButtonEnabler() {
        selectedItems.addListener((ListChangeListener<Topic>) e -> {
            int subscribedItems = 0;
            int unSubscribedItems = 0;
            for(Topic item : selectedItems) {
                if (item.isSubscribed()) {
                    subscribedItems++;
                } else {
                    unSubscribedItems++;
                }
            }
            unSubscribeBtn.setDisable(subscribedItems == 0);
            subscribeBtn.setDisable(unSubscribedItems == 0);

        });
    }

    /**
     * Initializes tha list where the topics of the table will be stored and monitors the list in the model for changes.
     */
    private void initTableList() {
        topicsList.addAll(model.getTopics().values());
        model.getTopics().addListener((MapChangeListener<String, Topic>) change -> {
            if (change.wasAdded()) {
                topicsList.add(change.getValueAdded());
            } else if (change.wasRemoved()) {
                topicsList.remove(change.getValueRemoved());
            }
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
        model.subscribe(subscribeList);
    }

    /**
     * Unsubscribe from selection action handler.
     */
    @FXML
    private void unSubscribeFromSelection() {
        ArrayList<Topic> unSubscribeList = selectedItems.stream()
                .filter(topic -> topic.isSubscribed())
                .collect(Collectors.toCollection(ArrayList::new));
        model.unsubscribe(unSubscribeList);
    }

    /**
     * Create new topic button handler.
     * @throws IOException
     */
    @FXML
    private void createNewTopic() throws IOException {
        CreateTopicDialog dialog = new CreateTopicDialog(AppMain.getStage());
        dialog.showAndWait();

    }

    /**
     * Delete selected topics button handler.
     */
    @FXML
    private void deleteSelectedTopics() {
        //TODO:Add confirm dialog and action to delete topics.
    }
}
