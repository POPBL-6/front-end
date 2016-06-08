package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import model.AppMain;
import model.FXModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Controller to load the basic interface and then load all the section controllers from it.
 * @author Gorka Olalde
 */
public class MainSceneController {

    private static final Logger logger = LogManager.getLogger(MainSceneController.class);

    private FXModel model;

    @FXML
    private Tab advancedTab;

    @FXML
    private Tab floor1Tab;

    /**
     * Sets the model to be used in the Controller.
     * @param model
     */
    public void setModel(FXModel model) {
        this.model = model;
    }

    /**
     * Initializes the all the tabs shown in the interface.
     */
    public void initTabs() {
        initFloor1Tab();
        initAdvancedViewTab();
    }

    /**
     * Initializes the tab for the first floor.
     * //TODO: Make floor tab loading dynamic based on the found fxml floor files.
     */
    private void initFloor1Tab() {
        String floorFileName = "/fxml/floor1.fxml";
        FloorController floor1Controller = new FloorController();
        floor1Controller.setModel(model);
        try {
            floor1Tab.setContent(floor1Controller.initController(floorFileName));
        } catch (IOException e) {
            logger.error("Error loading controller for floor " + floorFileName);
        }
    }


    /**
     * Initialize the controller of the Advanced Tab and load it.
     */
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
