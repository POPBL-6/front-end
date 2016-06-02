package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
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

    @FXML
    private Tab advancedTab;

    @FXML
    private Tab floor1Tab;

    public void setModel(FXModel model) {
        this.model = model;
    }

    public void initTabs() {
        initFloor1Tab();
        initAdvancedViewTab();
    }

    private void initFloor1Tab() {
        String floorFileName = "/fxml/floor1.fxml";
        FloorController floor1Controller = new FloorController();
        try {
            floor1Tab.setContent(floor1Controller.initController(floorFileName));
        } catch (IOException e) {
            logger.error("Error loading controller for floor " + floorFileName);
        }
    }

    private void initFloor2Tab() {
        //TODO: Add floor controller and init method.
    }

    private void initAdvancedViewTab() {
        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("/fxml/advanced.fxml"));
        try {
            advancedTab.setContent(loader.load());
            AdvancedTabController advancedTabController = loader.getController();
            advancedTabController.initController(model);
        } catch (IOException e) {
            logger.fatal("Error when loading advanced view FXML file");
        }

    }
}
