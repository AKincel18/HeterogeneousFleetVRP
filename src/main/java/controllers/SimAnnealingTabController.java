package controllers;

import algorithms.simulatedannealing.SimulatedAnnealingAlgorithm;
import algorithms.simulatedannealing.model.ParametersSimulatedAnnealing;
import commons.CustomAlert;
import commons.TextFieldDouble;
import commons.TextFieldInteger;
import commons.UtilsController;
import exceptions.InputException;
import input.DataReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.net.URL;
import java.util.ResourceBundle;

public class SimAnnealingTabController extends UtilsController implements Initializable {

    @FXML private TextFieldDouble probabilityField;
    @FXML private TextFieldInteger t0IterationNumberField;
    @FXML private TextFieldDouble coolingFactorField;
    @FXML private TextFieldInteger iterationNumberField;
    @FXML private Button startButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startButtonListener();
    }

    private void startButtonListener() {
        startButton.setOnAction(event -> startAlgorithm());
    }

    private void startAlgorithm() {
        try {
            validateInput();
            DataReader dataReader = getInputData();
            new SimulatedAnnealingAlgorithm(
                    dataReader.getCities(),
                    dataReader.getVehicles(),
                    dataReader.getDepot(),
                    new ParametersSimulatedAnnealing(
                            probabilityField.getValue(),
                            t0IterationNumberField.getValue(),
                            coolingFactorField.getValue(),
                            iterationNumberField.getValue()
                    )
            ).start();
        } catch (InputException e) {
            new CustomAlert(Alert.AlertType.ERROR, e.getHeaderError(), e.getContentError(), ButtonType.OK).show();
        }
    }

}
