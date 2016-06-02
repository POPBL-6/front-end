package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import model.AppMain;
import model.FXModel;
import model.datatypes.Device;

import java.io.IOException;
import java.util.List;


/**
 * Created by Gorka Olalde on 2/6/16.
 */
public class FloorController {

    FXModel model;
    Node rootNode;

    public Node initController(String floorFXMLFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource(floorFXMLFile));
        rootNode = loader.load();
        loader.setController(this);
        return rootNode;
    }

    public void setModel(FXModel model) {
        this.model = model;
    }

}
