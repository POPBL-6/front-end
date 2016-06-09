package controllers;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.AppMain;
import model.FXModel;
import model.datatypes.Device;
import model.datatypes.Topic;

import java.io.IOException;


/**
 * Controller to show the floor plan with all it's devices and
 * to configure them based in the data stored in the FXML file.
 * That way, the floor plan can be designed in sceneBuilder
 * and then the devices corresponding to each control can be added
 * in the FXML file as control userData.
 * @author Gorka Olalde
 */
public class FloorController {

    private FXModel model;
    private AnchorPane rootNode;
    private final ColorAdjust colorAdjust = new ColorAdjust();

    @FXML
    private ImageView floorPlan;

    private ObservableList<Node> deviceControls;


    /**
     * Initializes the controller by loading the FXML file that contains the view and the related devices to each control.
     * @param floorFXMLFile The floor plan FXML file.
     * @return The root node of the view.
     * @throws IOException If the FXML file can't be loaded.
     */
    public Node initController(String floorFXMLFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource(floorFXMLFile));
        rootNode = loader.load();
        loader.setController(this);
        initDevices();
        return rootNode;
    }


    /**
     * Sets the application model to be used in the controller.
     * @param model
     */
    public void setModel(FXModel model) {
        this.model = model;
    }


    /**
     * Initializes the devices contained in the floor plan based on their Control types.
     */
    private void initDevices() {
        deviceControls = rootNode.getChildren();
        for (Node node : deviceControls) {
            if (node instanceof JFXToggleButton) {
                configureToggleDevice((JFXToggleButton) node);
            } else if (node instanceof JFXSlider) {
                configureSliderDevice((JFXSlider) node);
            } else if (node instanceof JFXTextField) {
                configureTextDevice((JFXTextField) node);
            } else if (node instanceof ImageView && "floorPlan".equals(node.getId())) {
                floorPlan = (ImageView)node;
            }
        }
    }

    /**
     * Configures a device that will be shown as a TextField by subscribing it to the corresponding Topic.
     * @param textField The texfield control to be configured and that contains the device data.
     */
    private void configureTextDevice(JFXTextField textField) {
        Device device = (Device) textField.getUserData();
        if (device != null) {
            Topic deviceTopic = new Topic(device.getTopicName());
            model.subscribe(deviceTopic);
            textField.focusedProperty().addListener((o, oldVal, newVal) -> {
                if(!newVal) {
                    deviceTopic.setLastValue(textField.getText());
                    model.publish(deviceTopic);
                }
            });
            deviceTopic.lastValueProperty().addListener((o, oldVal, newVal) -> {
                textField.textProperty().set((String) newVal);
            });
        }
    }

    /**
     * Configures a device that will be shown as a slider in the view. If the device topic is named lightIntensity
     * it will call the corresponding method to set this particular device.
     * @param slider The slider to be configured.
     */
    private void configureSliderDevice(JFXSlider slider) {
        Device device = (Device) slider.getUserData();
        if (device != null) {
            Topic deviceTopic = new Topic(device.getTopicName());
            model.subscribe(deviceTopic);
            slider.valueChangingProperty().addListener((o, oldVal, newVal) -> {
                if(!newVal) {
                    deviceTopic.setLastValue(slider.getValue());
                    model.publish(deviceTopic);
                }
            });
            deviceTopic.lastValueProperty().addListener((o, oldVal, newVal) -> {
                slider.valueProperty().set((Double) newVal);
            });
            if(deviceTopic.getTopicName().contains("lightIntensity")) {
                initBrightnessDevice(deviceTopic);
            }
        }
    }

    /**
     * Configures a toggle device that will receive boolean values to be subscribed to the topic and listen to those values.
     * @param toggleButton The toggle to be configured.
     */
    private void configureToggleDevice(JFXToggleButton toggleButton) {
        Device device = (Device) toggleButton.getUserData();
        if (device != null) {
            Topic deviceTopic = new Topic(device.getTopicName());
            model.subscribe(deviceTopic);
            toggleButton.selectedProperty().addListener((o, oldVal, newVal) -> {
                deviceTopic.setLastValue(newVal);
                model.publish(deviceTopic);
            });
            deviceTopic.lastValueProperty().addListener((o, oldVal, newVal) -> {
                toggleButton.selectedProperty().set((boolean) newVal);
            });
        }
    }


    /**
     * Initializes the floor brightness device to set the floor plan image brightness corresponding to the received value.
     * @param lightTopic The topic to be subscribed to.
     */
    private void initBrightnessDevice(Topic lightTopic) {
        final int maxSliderRange = 100;
        final int minSliderOutput = 50;
        final int minBrightnessRange = -1;
        floorPlan.effectProperty().set(colorAdjust);
        lightTopic.lastValueProperty().addListener((o, oldVal, newVal) -> {
            Double input = (Double)newVal + minSliderOutput;
            Double mapping = input / maxSliderRange + minBrightnessRange;
            colorAdjust.setBrightness(mapping);
        });
    }

}
