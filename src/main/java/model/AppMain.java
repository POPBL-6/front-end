package model;

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
    private static final int HSIZE = 1366;
    private static final int VSIZE = 768;

    private FXModel model;
    private static Stage stage;
    private Scene scene;
    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        initModel();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();
        MainSceneController mainSceneController = loader.getController();
        mainSceneController.setModel(model);
        mainSceneController.initTabs();
        primaryStage.setTitle("POPBL6 Middleware Parking Demo App");
        scene = new Scene(root, HSIZE, VSIZE);
        scene.getStylesheets().add("/css/mainCss.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        logger.info("Application started");
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void initModel() {
        model = new FXModel();
        model.initModel(new FakePSPort());
    }
}


