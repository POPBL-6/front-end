package controllers.dialogs;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Class that extends the default DialogPane and creates JFXButtons instead of the default Buttons.
 */
class JFXDialogPane extends DialogPane {
    private CreateTopicDialog dialog;

    void setDialog(CreateTopicDialog dialog) { this.dialog = dialog; }

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
        ButtonBar.setButtonData(btn, type.getButtonData());
        btn.setDefaultButton(type != null && buttonData.isDefaultButton());
        btn.setCancelButton(type != null && buttonData.isCancelButton());
        btn.addEventHandler(ActionEvent.ACTION, ae -> {
            if (ae.isConsumed()) return;
            if (dialog != null) {
                dialog.impl_setResultAndClose(type, true);
            }
        });
        return btn;
    }
}
