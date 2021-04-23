package commons;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class CustomAlert extends Alert {

    public CustomAlert(AlertType alertType, String headerMsg, String contentMsg, ButtonType... buttons) {
        super(alertType, contentMsg, buttons);
        this.setHeaderText(headerMsg);
    }
}
