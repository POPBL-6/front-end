package model;

import api.PSPort;
import controllers.MainSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppMain extends Application {

    private final Logger logger = LogManager.getRootLogger();
    private FXModel model;
    private PSPort middleware;

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            initModel();
        } catch (Throwable throwable) {
            logger.fatal("Could not initialize model", throwable);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();
        MainSceneController mainSceneController = loader.getController();
        mainSceneController.setModel(model);
        mainSceneController.initTabs();
        primaryStage.setTitle("POPBL6 Middleware Parking Demo App");
        primaryStage.setScene(new Scene(root, 890, 555));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void initModel() {
        model = new FXModel();
        model.initModel(new FakePSPort());
    }
}


