package controllers.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AppMain;
import model.Topic;
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
    JFXTextField topicNameField;

    @FXML
    RequiredFieldValidator nameValidator;

    @FXML
    JFXComboBox<String> dataTypeCombo;

    @FXML
    JFXTextField valueField;

    @FXML
    ValidDataValidator valueValidator;

    String dataType = "String";

    JFXButton saveBtn;

    public CreateTopicDialog(Stage stage) {
        super();
        initOwner(stage);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Create a new topic");
        setHeight(350);
        setWidth(400);
        setDialogPane();
        initButtons();
        initDialogContent();
        initComboBox();
        initValidators();
        setButtonActionHandlers();
    }

    /**
     * Initializes the save and cancel buttons. Sets the save button to a variable for later interaction with it.
     */
    private void initButtons() {
        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        saveBtn = (JFXButton) getDialogPane().lookupButton(ButtonType.OK);
    }

    /**
     * Sets the dialog pane to the custom JFXDialogPane class which uses JFXButtons instead of the standard buttons.
     * @see JFXDialogPane
     */

    private void setDialogPane() {
        dialogPaneProperty().set(new JFXDialogPane());
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
        dataTypeCombo.selectionModelProperty().addListener(InvalidationListener -> {
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
            if(!newVal) topicNameField.validate();
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
        valueValidator.hasErrorsProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                if (!nameValidator.getHasErrors()) {
                    saveBtn.setDisable(true);
                }
            }
        });
    }

    /**
     * Sets the actions for the dialog buttons.
     */
    private void setButtonActionHandlers() {
        setResultConverter(button -> {
            Topic topic = new Topic();
            if (button == ButtonType.OK) {
                topic.setTopicName(topicNameField.getText());
                topic.setLastValue(createObjectByType(dataType, valueField.getText()));
                return topic;
            }
            return null;
        });
    }

    /**
     * Creates a new object of type x depending on the dataType.
     * Returns a new object depending of which datatype has been entered and the input value.
     * @param dataType The type of the data to return.
     * @param input The input data to be parsed.
     * @return The object containing the data.
     */

    private Object createObjectByType(String dataType, String input) {
        Object outputObject;
        switch (dataType) {
            case "String":
                outputObject = input;
                break;
            case "Integer":
                outputObject = Integer.parseInt(input);
                break;
            case "Double":
                outputObject = Double.parseDouble(input);
                break;
            case "Boolean":
                outputObject = Boolean.parseBoolean(input);
                break;
            default:
                outputObject = input;
        }
        return outputObject;
    }

}

