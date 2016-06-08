package controllers.dialogs;

import api.PSPort;
import api.PSPortFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controllers.validators.ValidTypeValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import model.AppMain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Dialog class to show the application start middleware setup dialog.
 * @author Gorka Olalde
 */
public class ConfigDialog extends Dialog<PSPort> {

    private static final Logger LOGGER = LogManager.getLogger(ConfigDialog.class);

    JFXButton saveBtn;

    @FXML
    Label headerLabel;

    @FXML
    JFXTextField addressField;

    @FXML
    JFXTextField portField;

    @FXML
    JFXComboBox<String> typeCombo;

    @FXML
    ValidTypeValidator addressValidator;

    @FXML
    ValidTypeValidator portValidator;

    /**
     * Default constructor of the config dialog. Initializes the dialog and all it's components.
     */
    public ConfigDialog() {
        super();
        setTitle("Please, enter middleware configuration");
        initDialogPane();
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
        headerLabel.setGraphic(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.PLUG, "54"));

    }


    /**
     * Initializes the save button.
     * Sets an action filter to the button to validate the input before submitting.
     */
    private void initButtons() {
        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        saveBtn = (JFXButton) getDialogPane().lookupButton(ButtonType.OK);
        saveBtn.addEventFilter(EventType.ROOT, event -> {
            if(!addressField.validate() || !portField.validate()) {
                event.consume();
            }
        });
    }


    /**
     * Configures the combobox that show the middleware implementation types.
     */
    private void initComboBox() {
        typeCombo.getItems().addAll("PSPortTCP", "PSPortSSL");
        typeCombo.selectionModelProperty().get().select(0);
    }


    /**
     * Configurates the validators used to validate the address and port inputs.
     */
    private void initValidators() {
        addressValidator.setMessage("Please, enter a valid address");
        addressValidator.setIcon(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.WARNING));
        addressValidator.setDataType("InetAddress");
        addressField.setValidators(addressValidator);
        addressField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if(!newVal) {
                addressField.validate();
            }
        });

        portValidator.setMessage("Please, enter a valid port number");
        portValidator.setIcon(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.WARNING));
        portField.setValidators(portValidator);
        portField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                portField.validate();
            }
        });
    }


    /**
     * Sets the dialog pane to the content read from the FXML file and sets this as it;s controller.
     */
    private void initDialogPane() {
        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("/fxml/configDialog.fxml"));
        JFXDialogPane dialogPane = new JFXDialogPane();
        loader.setController(this);
        try {
            dialogPane.setContent(loader.load());
        } catch (IOException e) {
            LOGGER.error("Unable to load config dialog FXML file", e);
        }
        dialogPane.setDialog(this);
        dialogPaneProperty().set(dialogPane);
    }


    /**
     * Configures the result converter of the dialog.
     */
    private void initResultConverter() {
        setResultConverter( button -> {
            PSPort psPort;
            String params;
            if (button == ButtonType.OK) {
                if (addressField.validate() && portField.validate()) {
                    params = typeCombo.getSelectionModel().getSelectedItem();
                    params += " -a " + addressField.getText();
                    params += " -p " + portField.getText();
                    try {
                        psPort = PSPortFactory.getPort(params);
                        return psPort;
                    } catch (Throwable throwable) {
                        LOGGER.fatal("Couldn't instantiate PSPort class", throwable);
                    }
                }
            }
            return null;
        });
    }




}
