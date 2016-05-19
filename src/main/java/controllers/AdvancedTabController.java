package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.fxml.FXML;
import model.FXModel;

/**
 * Created by Gorka Olalde on 19/5/16.
 */
public class AdvancedTabController {

    FXModel model;

    @FXML
    private JFXTextField searchField;

    @FXML
    private JFXSnackbar snackbar;

    @FXML
    private JFXTreeTableView<?> topicTable;

    @FXML
    private JFXButton createBtn;

    @FXML
    private JFXButton subscribeBtn;

    @FXML
    private JFXButton deleteBtn;

    public void setModel(FXModel model) {
        this.model = model;
    }


}
