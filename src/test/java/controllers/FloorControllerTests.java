package controllers;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import model.FXModel;
import model.datatypes.Device;
import model.datatypes.Topic;
import org.easymock.Capture;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import utils.JavaFXThreadingRule;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.TestCase.assertEquals;
import static org.easymock.EasyMock.*;

/**
 * Created by Gorka Olalde on 9/6/16.
 */
public class FloorControllerTests {

    private FloorController controller;

    private final FXModel model = createMock(FXModel.class);


    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    @Before
    public void before() {
        controller = new FloorController();
        controller.setModel(model);
    }

    @Test
    public void configureTextDeviceTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Device device = new Device();
        device.setTopicName("topic1");
        JFXTextField textField = new JFXTextField();
        textField.setUserData(device);
        Method initDevicesMethod = FloorController.class.getDeclaredMethod("configureTextDevice", JFXTextField.class);
        initDevicesMethod.setAccessible(true);
        Capture<Topic> deviceTopic = newCapture();
        //Record
        model.subscribe(capture(deviceTopic));
        replay(model);
        //Play
        initDevicesMethod.invoke(controller, textField);
        //Verify
        verify(model);
        assertEquals(device.getTopicName(), deviceTopic.getValue().getTopicName());
    }

    @Test
    public void configureSliderDeviceTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Device device = new Device();
        device.setTopicName("topic1");
        JFXSlider slider = new JFXSlider();
        slider.setUserData(device);
        Method initDevicesMethod = FloorController.class.getDeclaredMethod("configureSliderDevice", JFXSlider.class);
        initDevicesMethod.setAccessible(true);
        Capture<Topic> deviceTopic = newCapture();
        //Record
        model.subscribe(capture(deviceTopic));
        replay(model);
        //Play
        initDevicesMethod.invoke(controller, slider);
        //Verify
        verify(model);
        assertEquals(device.getTopicName(), deviceTopic.getValue().getTopicName());
    }

    @Test
    public void configureToggleDeviceTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Device device = new Device();
        device.setTopicName("topic1");
        JFXToggleButton toggleButton = new JFXToggleButton();
        toggleButton.setUserData(device);
        Method initDevicesMethod = FloorController.class.getDeclaredMethod("configureToggleDevice", JFXToggleButton.class);
        initDevicesMethod.setAccessible(true);
        Capture<Topic> deviceTopic = newCapture();
        //Record
        model.subscribe(capture(deviceTopic));
        replay(model);
        //Play
        initDevicesMethod.invoke(controller, toggleButton);
        //Verify
        verify(model);
        assertEquals(device.getTopicName(), deviceTopic.getValue().getTopicName());
    }

    @Test
    public void configureBrightnessDeviceTest() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Topic deviceTopic = new Topic("brightnessTopic");
        Double initBrightnessSlider = 50.0;
        Double finalBrightnessSlider = 100.0;
        Double initBrightness = 0.0;
        Double finalBrightness = 0.5;

        //Floor plan field
        Field floorPlanField = FloorController.class.getDeclaredField("floorPlan");
        ImageView floorPlan = new ImageView();
        floorPlanField.setAccessible(true);
        floorPlanField.set(controller, floorPlan);

        //InitBrightnewssDevice Method
        Method initBrightnessDevice = FloorController.class.getDeclaredMethod("initBrightnessDevice", Topic.class);
        initBrightnessDevice.setAccessible(true);

        //Test
        deviceTopic.setLastValue(initBrightnessSlider);
        initBrightnessDevice.invoke(controller, deviceTopic);
        ColorAdjust effect = (ColorAdjust) floorPlan.effectProperty().get();
        assertEquals(initBrightness, effect.getBrightness());
        deviceTopic.setLastValue(finalBrightnessSlider);
        assertEquals(finalBrightness, effect.getBrightness());
    }

    @Test
    public void initControllerTest() throws IOException {
        String fxmlFile = "/fxml/floor1.fxml";
        model.subscribe(anyObject(Topic.class));
        expectLastCall().atLeastOnce();
        replay(model);
        controller.initController(fxmlFile);
        verify(model);
    }

}
