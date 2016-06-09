package controllers;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.AppMain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Controller that handles a simple interface that is shown when the application initialization fails.
 * Shows the stacktrace of the exception that has caused the initialization to fail.
 * @author Gorka Olalde
 */
public class CrashController {

    private static final Logger LOGGER = LogManager.getLogger(CrashController.class);

    @FXML
    private JFXTextArea traceArea;

    /**
     * Loads the controller and the FXML file of the view.
     * @param exception The exception to be shown in the interface.
     * @return The root node of the view.
     */
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

    /**
     * Parses the stacktrace into a string.
     * @param e The exception to be parsed.
     */
    public void loadException(Exception e) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter, true);
        e.printStackTrace(printWriter);
        traceArea.setText(stringWriter.getBuffer().toString());
    }
}
