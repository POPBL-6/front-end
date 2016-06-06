package controllers;

import controllers.tableutils.DataTypeTableCell;
import controllers.tableutils.SubscribedTableCell;
import controllers.tableutils.TimestampTableCell;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.*;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.datatypes.Topic;

import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * Class for managing and creating the topic table.
 * @author Gorka Olalde
 */
class TopicTableManager {

    private final TableView topicTable;

    private ObservableList<Topic> topicList;

    private ObservableList<Topic> selectedItems;

    private TableColumn<Topic, String> topicNameColumn;

    private TableColumn<Topic, Boolean> subscribedColumn;

    private TableColumn<Topic, Long> timestampColumn;

    private TableColumn<Topic, Object> dataTypeColumn;

    private TableColumn<Topic, Object> valueColumn;


    private final BooleanProperty hasItemsToSubscribe = new SimpleBooleanProperty();

    private final BooleanProperty hasItemsToUnsubscribe = new SimpleBooleanProperty();


    public TopicTableManager(TableView<Topic> topicTable) {
        this.topicTable = topicTable;
    }

    public void initialize(ObservableMap<String, Topic> topicMap) {
        setTableItemList(topicMap);
        initialize();
    }

    public void initialize(ObservableList<Topic> topicList) {
        setTableItemList(topicList);
        initialize();
    }

    protected BooleanProperty hasItemsToSubscribeProperty() { return hasItemsToSubscribe; }

    protected BooleanProperty hasItemsToUnsubscribeProperty() { return hasItemsToUnsubscribe; }

    protected ObservableList<Topic> selectedItemsProperty() { return selectedItems; }

    private void initialize() {
        initColumns();
        initColumnDataSources();
        initCellRenderers();
        initTableLists();
        initSelectedItemsSubscriptionMonitor();
    }

    /**
     * Initializes the columns of the topic table and binds them with the properties inside the Topic class.
     * @see Topic
     */
    private void initColumns() {
        topicNameColumn = new TableColumn<>("Topic Name");
        topicNameColumn.setPrefWidth(300);

        subscribedColumn = new TableColumn<>("Is subscribed?");
        subscribedColumn.setPrefWidth(180);

        timestampColumn = new TableColumn<>("Timestamp");
        timestampColumn.setPrefWidth(180);

        dataTypeColumn = new TableColumn<>("Data type");
        dataTypeColumn.setPrefWidth(150);

        valueColumn = new TableColumn<>("Last value");
        valueColumn.setPrefWidth(200);

        topicTable.getColumns().addAll(
                topicNameColumn,
                subscribedColumn,
                timestampColumn,
                dataTypeColumn,
                valueColumn
        );
    }

    private void initColumnDataSources() {
        topicNameColumn.setCellValueFactory(new PropertyValueFactory<>("topicName"));
        subscribedColumn.setCellValueFactory(new PropertyValueFactory<>("isSubscribed"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("lastValueTimestamp"));
        dataTypeColumn.setCellValueFactory(new PropertyValueFactory<>("lastValue"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("lastValue"));
    }

    /**
     * Initializes the cell renderers of the table.
     */
    private void initCellRenderers() {
        dataTypeColumn.setCellFactory(column -> new DataTypeTableCell());
        subscribedColumn.setCellFactory(column -> new SubscribedTableCell());
        timestampColumn.setCellFactory(column -> new TimestampTableCell());
    }

    private void initTableLists() {
        topicTable.setItems(topicList);
        selectedItems = topicTable.getSelectionModel().getSelectedItems();
    }


    /**
     * Set table item list based on an external ObservableMap.
     * @param topicMap The external topic ObservableMap.
     */
    private void setTableItemList(ObservableMap<String, Topic> topicMap) {
        if (topicList == null) {
            topicList = FXCollections.observableArrayList();
        } else {
            topicList.clear();
        }
        topicList.addAll(topicMap.values());
        topicMap.addListener((MapChangeListener<String, Topic>) change -> {
            if (change.wasAdded()) {
                topicList.add(change.getValueAdded());
            } else if (change.wasRemoved()) {
                topicList.remove(change.getValueRemoved());
            }
        });
    }

    /**
     * Set table item list to be based on an external ObservableList.
     * @param topicList The external ObservableList.
     */
    private void setTableItemList(ObservableList<Topic> topicList) {
        if (topicList == null) {
            this.topicList = topicList;
        } else {
            this.topicList.clear();
            this.topicList.addAll(topicList);
        }
    }

    /**
     * Create listener to check if any of the selected items is suitable for unsubscribing from it or subscribing to it.
     * Depending of the current subscribe state of the selected topics in the table, sets their respective properties.
     */
    private void initSelectedItemsSubscriptionMonitor() {
        topicTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
       selectedItems.addListener((ListChangeListener<Topic>) e -> {
            int subscribedItems = 0;
            int unSubscribedItems = 0;
            for(Topic item : selectedItems) {
                if(item != null) {
                    if (item.isSubscribed()) {
                        subscribedItems++;
                    } else {
                        unSubscribedItems++;
                    }
                }
            }
            hasItemsToUnsubscribe.set(subscribedItems != 0);
            hasItemsToSubscribe.set(unSubscribedItems != 0);
        });
    }

    public void clearTableSelection() {
        topicTable.getSelectionModel().clearSelection();
    }

    public void search(String topic) {
        topicTable.getSelectionModel().clearSelection();
        topicList.stream()
                .filter(s -> s.getTopicName().contains(topic))
                .forEach(s -> topicTable.getSelectionModel().select(s));
    }


}
