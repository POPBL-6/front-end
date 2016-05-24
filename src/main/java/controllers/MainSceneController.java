package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import model.AppMain;
import model.FXModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by Gorka Olalde on 19/5/16.
 */
public class MainSceneController {

    private static final Logger logger = LogManager.getLogger(MainSceneController.class);

    private FXModel model;
    private AdvancedTabController advancedTabController;

    @FXML
    StackPane mainRootNode;

    @FXML
    private Tab floor1Tab;

    @FXML
    private Group floor1Group;

    @FXML
    private Tab floor2Tab;

    @FXML
    private Group floor2Group;

    @FXML
    private Tab advancedTab;

    @FXML
    private Group advancedTabGroup;

    public void setModel(FXModel model) {
        this.model = model;
    }

    public void initTabs() {
        initAdvancedViewTab();
    }

    public void initFloor1Tab() {
        
    }

    public void initFloor2Tab() {

    }

    private void initAdvancedViewTab() {
        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("/fxml/advanced.fxml"));
        try {
            Node advancedTabNode = loader.load();
            advancedTabNode.autosize();
            advancedTabGroup.getChildren().add(advancedTabNode);
            advancedTabController = loader.getController();
            advancedTabController.setMainRootNode(mainRootNode);
            advancedTabController.initController(model);
        } catch (IOException e) {
            logger.fatal("Error when loading advanced view FXML file");
        }

    }
}
