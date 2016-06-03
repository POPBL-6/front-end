package controllers;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import model.AppMain;
import model.FXModel;
import model.datatypes.Device;
import model.datatypes.Topic;

import java.io.IOException;


/**
 * Created by Gorka Olalde on 2/6/16.
 */
public class FloorController {

    private FXModel model;
    private AnchorPane rootNode;

    private ObservableList<Node> deviceControls;

    public Node initController(String floorFXMLFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource(floorFXMLFile));
        rootNode = loader.load();
        loader.setController(this);
        initDevices();
        return rootNode;
    }

    public void setModel(FXModel model) {
        this.model = model;
    }

    private void initDevices() {
        deviceControls = rootNode.getChildren();
        for (Node node : deviceControls) {
            if (node instanceof JFXToggleButton) {
                configureToggleDevice((JFXToggleButton) node);
            } else if (node instanceof JFXSlider) {
                configureSliderDevice((JFXSlider) node);
            } else if (node instanceof JFXTextField) {
                configureTextDevice((JFXTextField) node);
            }
        }
    }

    private void configureTextDevice(JFXTextField textField) {
        Device device = (Device) textField.getUserData();
        if (device != null) {
            Topic deviceTopic = new Topic(device.getTopicName());
            model.subscribe(deviceTopic);
            textField.textProperty().addListener((o, oldVal, newVal) -> {
                deviceTopic.setLastValue(newVal);
                model.publish(deviceTopic);
            });
            deviceTopic.lastValueProperty().addListener((o, oldVal, newVal) -> {
                textField.textProperty().set((String) newVal);
            });
        }
    }

    private void configureSliderDevice(JFXSlider slider) {
        Device device = (Device) slider.getUserData();
        if (device != null) {
            Topic deviceTopic = new Topic(device.getTopicName());
            model.subscribe(deviceTopic);
            slider.valueProperty().addListener((o, oldVal, newVal) -> {
                deviceTopic.setLastValue(newVal);
                model.publish(deviceTopic);
            });
            deviceTopic.lastValueProperty().addListener((o, oldVal, newVal) -> {
                slider.valueProperty().set((Double) newVal);
            });
        }
    }

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


}
