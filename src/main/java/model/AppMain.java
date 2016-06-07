package model;

import api.PSPort;
import controllers.CrashController;
import controllers.MainSceneController;
import controllers.dialogs.ConfigDialog;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class AppMain extends Application {

    private final Logger logger = LogManager.getRootLogger();
    private static final int HSIZE = 1366;
    private static final int VSIZE = 768;

    private FXModel model;
    private static Stage stage;
    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage){
        Parent root;
        stage = primaryStage;
        if (initModel()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                root = loader.load();
                MainSceneController mainSceneController = loader.getController();
                mainSceneController.setModel(model);
                mainSceneController.initTabs();
            } catch (IOException e) {
                CrashController crashController = new CrashController();
                root = crashController.loadController(e);
            }
            primaryStage.setTitle("POPBL6 Middleware Parking Demo App");
            Scene scene = new Scene(root, HSIZE, VSIZE);
            scene.getStylesheets().add("/css/mainCss.css");
            primaryStage.setScene(scene);
            primaryStage.show();
            logger.info("Application started");
        } else {
            stage.close();
            logger.info("Application terminated due to user cancellation in config dialog");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    private boolean initModel() {
        boolean initializedOK = false;
        model = new FXModel();
        PSPort psPort;
        ConfigDialog dialog = new ConfigDialog();
        dialog.showAndWait();
        psPort = dialog.getResult();
        if (psPort != null) {
            model.initModel(psPort);
            initializedOK = true;
        }
        return initializedOK;
    }
}


