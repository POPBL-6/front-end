package controllers.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.datatypes.Topic;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.JavaFXThreadingRule;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertNull;

/**
 * Created by Gorka Olalde on 9/6/16.
 */
@RunWith(Parameterized.class)
public class EditTopicInvalidTest {

    private JFXTextField newValueField;

    private JFXButton saveBtn;

    private EditTopicDialog dialog;

    private Stage stage;
    @Parameterized.Parameter
    public Topic inputTopic;

    @Parameterized.Parameter(value = 1)
    public Object existingObject;

    @Parameterized.Parameter(value = 2)
    public String newValue;

    @Parameterized.Parameters
    public static Collection inputs() {
        return Arrays.asList(new Object[][]{
                {new Topic("test"), "test", ""},
                {new Topic("test"), 10, "invalid"},
                {new Topic("test"), 10.5, "invalid"},
                {new Topic("test"), true, "invalid"},
                {new Topic("test"), null, ""}
        });
    }
    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    @Before
    public void before() throws NoSuchFieldException, IllegalAccessException {
        stage = new Stage();
        Scene scene = new Scene(new AnchorPane());
        stage.setScene(scene);
        inputTopic.setLastValue(existingObject);
        dialog = new EditTopicDialog(stage, inputTopic);
        getDialogFields();
    }

    private void getDialogFields() throws NoSuchFieldException, IllegalAccessException {
        //Input TextField
        Field newValueFieldField = EditTopicDialog.class.getDeclaredField("newValueField");
        newValueFieldField.setAccessible(true);
        newValueField = (JFXTextField) newValueFieldField.get(dialog);

        //Save button
        saveBtn = (JFXButton) dialog.getDialogPane().lookupButton(ButtonType.OK);
    }

    @Test
    public void testValidInputs() {
        newValueField.setText(newValue);
        saveBtn.fire();
        assertNull(dialog.getResult());
    }
}
