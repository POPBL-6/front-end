package controllers.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.JavaFXThreadingRule;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Gorka Olalde on 8/6/16.
 */
@RunWith(Parameterized.class)
public class CreateTopicDialogValidTest {

    private CreateTopicDialog dialog;

    private JFXTextField topicNameField;

    private JFXComboBox dataTypeCombo;

    private JFXTextField valueField;

    private JFXButton saveBtn;

    @Parameterized.Parameter
    public String topicName;

    @Parameterized.Parameter(value = 1)
    public int dataType;
    @Parameterized.Parameter(value = 2)
    public String inputValue;

    @Parameterized.Parameter(value = 3)
    public Object expected;

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    @Parameterized.Parameters
    public static Collection inputs() {
        return Arrays.asList(new Object[][]{
                {"test", 0, "test", "test"},
                {"test", 1, "10", 10},
                {"test", 2, "10.5", 10.5},
                {"test", 3, "true", true}
        });
    }

    @Before
    public void before() throws NoSuchFieldException, IllegalAccessException {
        Stage stage = new Stage();
        Scene scene = new Scene(new AnchorPane());
        stage.setScene(scene);
        dialog = new CreateTopicDialog(stage);
        getDialogFields();
    }

    private void getDialogFields() throws NoSuchFieldException, IllegalAccessException {
        //Topic name TextField
        Field topicNameFieldFIeld = CreateTopicDialog.class.getDeclaredField("topicNameField");
        topicNameFieldFIeld.setAccessible(true);
        topicNameField = (JFXTextField) topicNameFieldFIeld.get(dialog);

        //Data Type ComboBox
        Field dataTypeComboField = CreateTopicDialog.class.getDeclaredField("dataTypeCombo");
        dataTypeComboField.setAccessible(true);
        dataTypeCombo = (JFXComboBox) dataTypeComboField.get(dialog);

        //Value TextField
        Field valueFieldField = CreateTopicDialog.class.getDeclaredField("valueField");
        valueFieldField.setAccessible(true);
        valueField = (JFXTextField) valueFieldField.get(dialog);

        //Save Button
        saveBtn = (JFXButton) dialog.getDialogPane().lookupButton(ButtonType.OK);
    }

    @Test
    public void validTopicTest() {
        topicNameField.setText(topicName);
        dataTypeCombo.getSelectionModel().select(dataType);
        valueField.setText(inputValue);
        saveBtn.fire();
        assertNotNull(dialog.getResult());
        assertEquals(topicName, dialog.getResult().getTopicName());
        assertEquals(expected, dialog.getResult().getLastValue());
    }

}
