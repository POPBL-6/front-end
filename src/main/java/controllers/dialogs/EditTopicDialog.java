package controllers.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.validators.ValidTypeValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.AppMain;
import model.DataUtils;
import model.datatypes.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by Gorka Olalde on 7/6/16.
 */
public class EditTopicDialog extends Dialog<Topic> {

    private static final Logger LOGGER = LogManager.getLogger(EditTopicDialog.class);

    @FXML
    private Label headerLabel;

    @FXML
    private Label oldValueLabel;

    @FXML
    private JFXTextField newValueField;

    @FXML
    private ValidTypeValidator valueValidator;

    private Topic topic;

    private String dataType;


    public void setTopic(Topic topic) {
        this.topic = topic;
        dataType = DataUtils.getStringDataType(topic.getLastValue());
    }

    public EditTopicDialog(Stage stage, Topic topic) {
        super();
        setTopic(topic);
        initDialogPane();
        initDialogContent();
        initLabels();
        initButtons();
        initValidators();
        setButtonActionHandlers();
    }


    private void initDialogPane() {
        JFXDialogPane dialogPane = new JFXDialogPane();
        dialogPane.setDialog(this);
        dialogPaneProperty().set(dialogPane);
    }

    private void initDialogContent() {
        Node rootNode;
        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("/fxml/editDialog.fxml"));
        loader.setController(this);
        try {
            rootNode = loader.load();
            getDialogPane().setContent(rootNode);
        } catch (IOException e) {
            LOGGER.error("Unable to load edit dialog FXML file");
        }

    }

    private void initLabels() {
        headerLabel.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.PENCIL_SQUARE_ALT, "54"));
        if (topic.getLastValue() != null) {
            oldValueLabel.setText(topic.getLastValue().toString());
        } else {
            oldValueLabel.setText("Not initialized");
        }
    }

    private void initButtons() {
        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        JFXButton saveButton = (JFXButton) getDialogPane().lookupButton(ButtonType.OK);
        saveButton.addEventFilter(EventType.ROOT, event -> {
            if(!newValueField.validate()) {
                event.consume();
            }
        });
    }



    private void initValidators() {
        if (dataType.equals("Unknown")) {
            newValueField.setDisable(true);
        } else {
            valueValidator.setDataType(dataType);
            newValueField.setValidators(valueValidator);
            newValueField.textProperty().addListener((o, oldVal, newVal) -> newValueField.validate());
        }
    }
    private void setButtonActionHandlers() {
        setResultConverter( button -> {
            if (button == ButtonType.OK) {
                if (newValueField.validate() && !"Unknown".equals(dataType)) {
                    topic.setLastValue(DataUtils.createObjectByType(dataType, newValueField.getText()));
                    return topic;
                }
            }
            return null;
        });
    }
}
