package controllers.dialogs;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Class that extends the default DialogPane and creates JFXButtons instead of the default Buttons.
 */
public class JFXDialogPane extends DialogPane {
    private Dialog dialog;

    /**
     * Method to set the dialog that is using this dialog pane.
     * @param dialog
     */
    protected void setDialog(Dialog dialog) { this.dialog = dialog; }


    /**
     * Overridden method for creating the buttons of the dialog.
     * Creates JFXButtons instead of normal buttons.
     * The JFXButton implementation sets the ButtonType
     * to it's custom types so a workaround has been used
     * to call the result converter of the dialog.
     * @param type Type of button to be created.
     * @return The Node instance of the created button.
     */
    @Override
    protected Node createButton(ButtonType type) {
        final JFXButton btn = new JFXButton(type.getText());
        final ButtonBar.ButtonData buttonData = type.getButtonData();
        if (type == ButtonType.OK) {
            btn.setRipplerFill(Color.GREEN);
            btn.setFont(new Font("Arial", 18));
        } else if (type == ButtonType.CANCEL) {
            btn.setRipplerFill(Color.RED);
            btn.setFont(new Font("Arial", 18));
        } else {
            btn.setFont(new Font("Arial", 18));
        }
        ButtonBar.setButtonData(btn, buttonData);
        btn.setDefaultButton(type != null && buttonData.isDefaultButton());
        btn.setCancelButton(type != null && buttonData.isCancelButton());
        btn.addEventHandler(ActionEvent.ACTION, ae -> {
            if (ae.isConsumed()) {
                return;
            }
            if (dialog != null) {

                dialog.setResult(dialog.getResultConverter().call(ButtonType.OK));
                dialog.close();
            }
        });
        return btn;
    }
}
