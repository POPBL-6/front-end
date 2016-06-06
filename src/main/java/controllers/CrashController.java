package controllers;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import model.AppMain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Gorka Olalde on 6/6/16.
 */
public class CrashController {

    private static final Logger LOGGER = LogManager.getLogger(CrashController.class);

    @FXML
    private JFXTextArea traceArea;


    public Parent loadController(Exception exception) {
        Parent parent = null;
        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("/fxml/crash.fxml"));
        loader.setController(this);
        try {
            parent = loader.load();
        } catch (IOException e) {
            LOGGER.error("Error loading FXML resource for Crash controller", e);
        }
        loadException(exception);
        return parent;
    }

    public void loadException(Exception e) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter, true);
        e.printStackTrace(printWriter);
        traceArea.setText(stringWriter.getBuffer().toString());
    }
}
