package controllers.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import controllers.validators.ValidTypeValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AppMain;
import model.DataUtils;
import model.datatypes.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Dialog class for creating new topic custom dialogs using JavaFX native dialogs API.
 * @author Gorka Olalde
 */
public class CreateTopicDialog extends Dialog<Topic> {

    private static final Logger logger = LogManager.getLogger(CreateTopicDialog.class);

    @FXML
    private Label headerLabel;

    @FXML
    private JFXTextField topicNameField;

    @FXML
    private RequiredFieldValidator nameValidator;

    @FXML
    private JFXComboBox<String> dataTypeCombo;

    @FXML
    private JFXTextField valueField;

    @FXML
    private ValidTypeValidator valueValidator;

    private String dataType = "String";

    private JFXButton saveBtn;

    /**
     * Default constructor of the topic creation dialog. Initializes the dialog and all it's components.
     * @param stage The main stage of the application. Needed to set the modality of the dialog.
     */
    public CreateTopicDialog(Stage stage) {
        super();
        initOwner(stage);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Create a new topic");
        setHeight(350);
        setWidth(400);
        setDialogPane();
        initDialogContent();
        initHeaderIcon();
        initButtons();
        initComboBox();
        initValidators();
        initResultConverter();
    }


    /**
     * Loads the icon to be used in the Dialog title.
     */
    private void initHeaderIcon() {
        headerLabel.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.PAPER_PLANE, "54"));
    }


    /**
     * Initializes the save button.
     * Sets an action filter to the button to validate the input before submitting.
     */
    private void initButtons() {
        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        saveBtn = (JFXButton) getDialogPane().lookupButton(ButtonType.OK);
        saveBtn.addEventFilter(EventType.ROOT, event -> {
            if (!topicNameField.validate() || !valueField.validate()) {
                event.consume();
            }
        });
    }


    /**
     * Sets the dialog pane to the custom JFXDialogPane class which uses JFXButtons instead of the standard buttons.
     * @see JFXDialogPane
     */
    private void setDialogPane() {
        JFXDialogPane dialogPane = new JFXDialogPane();
        dialogPane.setDialog(this);
        dialogPaneProperty().set(dialogPane);
    }


    /**
     * Loads the dialog form from the FXML file and sets it to the DialogPane.
     * Also sets this class to be it's controller.
     */
    private void initDialogContent() {
        AnchorPane rootNode;
        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("/fxml/createDialog.fxml"));
        loader.setController(this);
        try {
            rootNode = loader.load();
            this.getDialogPane().setContent(rootNode);
        } catch (IOException e) {
            logger.error("The FXML loader was unable to load the createDialog.fxml file", e);
        }
    }


    /**
     * Initializes the combobox for choosing between the different topic data types. Exports the currectly selected
     * data type to a variable by using an Invalidation listener.
     */
    private void initComboBox() {
        dataTypeCombo.getItems().setAll("String", "Integer", "Double", "Boolean");
        dataTypeCombo.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
            dataType = dataTypeCombo.getSelectionModel().getSelectedItem();
        });
        dataTypeCombo.getSelectionModel().select(0);
    }


    /**
     * Initializes the validators for the topic name and data value.
     * Initializes the validators for checking that a topic name has been entered and that the value is valid.
     */
    private void initValidators() {
        nameValidator.setMessage("Please, enter a topic name");
        nameValidator.setIcon(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.WARNING));
        topicNameField.setValidators(nameValidator);
        topicNameField.focusedProperty().addListener((o,oldVal,newVal) -> {
            if(!newVal) {
                topicNameField.validate();
            }
        });

        valueValidator.setMessage("Please, enter a correct value");
        valueValidator.setIcon(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.WARNING));
        valueField.setValidators(valueValidator);
        valueField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if(!newVal) {
                valueValidator.setDataType(dataType);
                valueField.validate();
            }
        });
    }


    /**
     * Configures the result converter of the dialog.
     */
    private void initResultConverter() {
        setResultConverter( button -> {
            Topic topic = new Topic();
            if (button == ButtonType.OK) {
                valueValidator.setDataType(dataType);
                if (topicNameField.validate() && valueField.validate()) {
                    topic.setTopicName(topicNameField.getText());
                    topic.setLastValueTimestamp(System.currentTimeMillis());
                    topic.setLastValue(DataUtils.createObjectByType(dataType, valueField.getText()));
                    return topic;
                }
            }
            return null;
        });

    }



}

