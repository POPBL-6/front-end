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
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AppMain;
import model.DataUtils;
import model.datatypes.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Dialog to edit the value of the topics.
 * @author Gorka Olalde
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


    /**
     * Default constructor for the dialog. Needs the application stage and a topic.
     * @param stage The stage of the application for setting the modality.
     * @param topic
     */
    public EditTopicDialog(Stage stage, Topic topic) {
        super();
        initOwner(stage);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Edit topic " + topic.getTopicName());
        setTopic(topic);
        initDialogPane();
        initDialogContent();
        initLabels();
        initButtons();
        initValidators();
        initResultConverter();
    }


    /**
     * Sets the topic of the dialog and gets the data type from it.
     * @param topic The topic to be edited in the dialog.
     */
    public void setTopic(Topic topic) {
        this.topic = topic;
        dataType = DataUtils.getStringDataType(topic.getLastValue());
    }


    /**
     * Sets the dialog pane to the custom JFXDialogPane class which uses JFXButtons instead of the standard buttons.
     * @see JFXDialogPane
     */
    private void initDialogPane() {
        JFXDialogPane dialogPane = new JFXDialogPane();
        dialogPane.setDialog(this);
        dialogPaneProperty().set(dialogPane);
    }

    /**
     * Loads the dialog form from the FXML file and sets it to the DialogPane.
     * Also sets this class to be it's controller.
     */
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

    /**
     * Sets both the header label to use a custom icon and also the old value label to the value stored in the topic.
     */
    private void initLabels() {
        headerLabel.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.PENCIL_SQUARE_ALT, "54"));
        if (topic.getLastValue() != null) {
            oldValueLabel.setText(topic.getLastValue().toString());
        } else {
            oldValueLabel.setText("Not initialized");
        }
    }


    /**
     * Initializes the save button.
     * Sets an action filter to the button to validate the input before submitting.
     */
    private void initButtons() {
        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        JFXButton saveButton = (JFXButton) getDialogPane().lookupButton(ButtonType.OK);
        saveButton.addEventFilter(EventType.ROOT, event -> {
            if(!newValueField.validate()) {
                event.consume();
            }
        });
    }

    /**
     * Initializes the validators for checking that a correct value has been entered on the text field.
     */
    private void initValidators() {
        if (dataType.equals("Unknown")) {
            newValueField.setDisable(true);
        } else {
            valueValidator.setDataType(dataType);
            newValueField.setValidators(valueValidator);
            newValueField.textProperty().addListener((o, oldVal, newVal) -> newValueField.validate());
        }
    }

    /**
     * Configures the result converter of the dialog.
     */
    private void initResultConverter() {
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
