package controllers.dialogs;

import api.PSPortFactory;
import api.PSPortSSL;
import api.PSPortTCP;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controllers.dialogs.ConfigDialog;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import utils.JavaFXThreadingRule;

import java.lang.reflect.Field;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.*;

/**
 * Created by Gorka Olalde on 8/6/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Callback.class, PSPortTCP.class, PSPortSSL.class, PSPortFactory.class})
public class ConfigDialogControllerTest{

    ConfigDialog dialog;
    Label headerLabel;
    JFXComboBox typeCombo;
    JFXTextField addressField;
    JFXTextField portField;
    JFXButton saveButton;

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();


    @Before
    public void before() throws NoSuchFieldException, IllegalAccessException {
        Platform.runLater(() -> {});
        dialog = new ConfigDialog();
        getDialogControls();
        mockStatic(PSPortFactory.class);
        createMock(PSPortTCP.class);
        createMock(PSPortSSL.class);
    }

    public void getDialogControls() throws IllegalAccessException, NoSuchFieldException {
        //Header label
        Field headerLabelField = ConfigDialog.class.getDeclaredField("headerLabel");
        headerLabelField.setAccessible(true);
        headerLabel = (Label) headerLabelField.get(dialog);

        //Middleware type ComboBox
        Field typeComboField = ConfigDialog.class.getDeclaredField("typeCombo");
        typeComboField.setAccessible(true);
        typeCombo = (JFXComboBox) typeComboField.get(dialog);

        //Address field
        Field addressFieldField = ConfigDialog.class.getDeclaredField("addressField");
        addressFieldField.setAccessible(true);
        addressField = (JFXTextField) addressFieldField.get(dialog);

        //Port field
        Field portFieldField = ConfigDialog.class.getDeclaredField("portField");
        portFieldField.setAccessible(true);
        portField = (JFXTextField) portFieldField.get(dialog);

        //Save Button
        saveButton = (JFXButton) dialog.getDialogPane().lookupButton(ButtonType.OK);
    }


    @Test
    public void testValidPSPortTCP() throws Throwable {
        PSPortTCP portMock = createMock(PSPortTCP.class);
        addressField.setText("127.0.0.1");
        portField.setText("5434");
        typeCombo.getSelectionModel().select(0);
        //Record
        expect(PSPortFactory.getPort("PSPortTCP -a 127.0.0.1 -p 5434")).andReturn(portMock);
        replay(PSPortFactory.class, portMock);
        //Play
        saveButton.fire();
        verify(PSPortFactory.class, portMock);
        assertEquals(portMock, dialog.getResult());
    }

    @Test
    public void testValidPSPortSSL() throws Throwable {
        PSPortSSL portMock = createMock(PSPortSSL.class);
        addressField.setText("127.0.0.1");
        portField.setText("5434");
        typeCombo.getSelectionModel().select(1);
        //Record
        expect(PSPortFactory.getPort("PSPortSSL -a 127.0.0.1 -p 5434")).andReturn(portMock);
        replay(PSPortFactory.class, portMock);
        //Play
        saveButton.fire();
        verify(PSPortFactory.class, portMock);
        assertEquals(portMock, dialog.getResult());
    }

    @Test
    public void testInvalidAddress() {
        addressField.setText("420.420.420.420");
        portField.setText("5434");
        typeCombo.getSelectionModel().select(0);

        saveButton.fire();
        assertNull(dialog.getResult());
    }

    @Test
    public void testInvalidPort() {
        addressField.setText("127.0.0.1");
        portField.setText("65556");
        typeCombo.getSelectionModel().select(0);

        saveButton.fire();
        assertNull(dialog.getResult());
    }

    @Test
    public void testInvalidBoth() {
        addressField.setText("265.265.265.0");
        portField.setText("65556");
        typeCombo.getSelectionModel().select(0);

        saveButton.fire();
        assertNull(dialog.getResult());
    }
}
