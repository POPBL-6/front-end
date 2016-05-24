package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import model.FXModel;
import model.Topic;

/**
 * Created by Gorka Olalde on 24/5/16.
 */
public class CreateDialogController {

    private FXModel model;

    private StackPane mainRootNode;

    @FXML
    private StackPane rootNode;

    @FXML
    private JFXDialog dialog;

    @FXML
    private JFXTextField topicNameField;

    @FXML
    private RequiredFieldValidator nameValidator;

    @FXML
    private JFXComboBox<String> dataTypeCombo;

    @FXML
    private JFXTextField valueField;

    @FXML
    private RequiredFieldValidator valueValidator;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private void saveTopic(ActionEvent event) {
        Topic topic = new Topic();
        String topicName = topicNameField.getText();
        String dataType = dataTypeCombo.getSelectionModel().getSelectedItem();
        String value = valueField.getText();

        if (!"".equals(topicName) && !"".equals(value)) {
            topic.setTopicName(topicName);
            switch (dataType) {
                case "Integer":
                    topic.setLastValue(Integer.parseInt(value));
                    break;
                case "Boolean":
                    topic.setLastValue(Boolean.parseBoolean(value));
                    break;
                case "String":
                    topic.setLastValue(value);
                    break;
                default:
                    topic.setLastValue(value);
            }
            model.publish(topic);
        }

    }


    protected void setModel(FXModel model) {
        this.model = model;
    }

    protected void initController() {
        dataTypeCombo.getItems().addAll(
                "Integer"
                , "String"
                , "Boolean"
                , "Double"
        );
        initValidators();
    }

    private void initValidators() {
        nameValidator.setMessage("Please, enter a name");
        nameValidator.setIcon(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.WARNING));
        valueValidator.setMessage("Please, enter a value");
        valueValidator.setIcon(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.WARNING));
        topicNameField.setValidators(nameValidator);
        valueField.setValidators(valueValidator);

        nameValidator.focusedProperty().addListener((o,oldVal,newVal) -> {
            if(!newVal) topicNameField.validate();
        });
        valueValidator.focusedProperty().addListener((o,oldVal,newVal) -> {
            if(!newVal) valueField.validate();
        });
    }

    protected void setRootNode(StackPane node) {
        mainRootNode = node;
    }

    protected void showDialog() {
        dialog.show(mainRootNode);
    }

    protected void hideDialog() {
        dialog.close();
    }

}
