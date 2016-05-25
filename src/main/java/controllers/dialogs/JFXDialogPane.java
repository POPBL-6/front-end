package controllers.dialogs;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Class that extends the default DialogPane and creates JFXButtons instead of the default Buttons.
 */
class JFXDialogPane extends DialogPane {
    @Override
    protected Node createButton(ButtonType type) {
        JFXButton btn = new JFXButton();
        if (type == ButtonType.OK) {
            btn.setRipplerFill(Color.GREEN);
            btn.setText("Save");
            btn.setFont(new Font("Arial", 18));
        } else if (type == ButtonType.CANCEL) {
            btn.setRipplerFill(Color.RED);
            btn.setText("Cancel");
            btn.setFont(new Font("Arial", 18));
        }
        return btn;
    }
}
