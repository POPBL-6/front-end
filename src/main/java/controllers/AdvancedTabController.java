package controllers;

import com.jfoenix.controls.*;
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
    private CreateDialogController createDialogController;
    private StackPane mainRootNode;


    @FXML
    private StackPane rootNode;

    @FXML
    private JFXTextField searchField;

    @FXML
    private JFXSnackbar snackbar;

    @FXML
    private JFXButton createBtn;

    @FXML
    private JFXButton subscribeBtn;

    @FXML
    private JFXButton unSubscribeBtn;

    @FXML
    private JFXButton deleteBtn;

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



    protected void setModel(FXModel model) {
        this.model = model;
    }

    protected void setMainRootNode(StackPane rootNode) {
        this.mainRootNode = rootNode;
    }

    protected void initController() {
        initTableList();
        initTable();
        initColumns();
        initCellRenderers();
    }

    protected void initController(FXModel model) {
        setModel(model);
        initController();
    }

    private void initTable() {
        initTableList();
        topicTable.itemsProperty().set(topicsList);
        selectedItems = topicTable.getSelectionModel().getSelectedItems();
        createSubscriptionButtonEnabler();
    }

    private void initColumns() {
        topicNameColumn.setCellValueFactory(new PropertyValueFactory<>("topicName"));
        subscribedColumn.setCellValueFactory(new PropertyValueFactory<>("isSubscribed"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        dataTypeColumn.setCellValueFactory(new PropertyValueFactory<>("lastValue"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("lastValue"));
    }

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

    @FXML
    private void subscribeToSelection(ActionEvent event) {
        ArrayList<Topic> subscribeList = selectedItems.stream()
                .filter(topic -> !topic.isSubscribed())
                .collect(Collectors.toCollection(ArrayList::new));
        model.subscribe(subscribeList);
    }

    @FXML
    private void unSubscribeFromSelection(ActionEvent event) {
        ArrayList<Topic> unSubscribeList = selectedItems.stream()
                .filter(topic -> topic.isSubscribed())
                .collect(Collectors.toCollection(ArrayList::new));
        model.unsubscribe(unSubscribeList);
    }

    @FXML
    private void createNewTopic(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("/fxml/createDialog.fxml"));
        try {
            JFXDialog dialog = loader.load();
            createDialogController = loader.getController();
        } catch (IOException e) {
            logger.error("Error when loading create dialog FXML file", e);
            throw e;
        }
            createDialogController.setModel(model);
            createDialogController.setRootNode(mainRootNode);
            createDialogController.initController();
            createDialogController.showDialog();

    }

    @FXML
    private void deleteSelectedTopics(ActionEvent event) {
        //TODO:Add confirm dialog and action to delete topics.
    }
}
