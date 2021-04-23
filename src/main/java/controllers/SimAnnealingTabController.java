package controllers;

import algorithms.simulatedannealing.SimulatedAnnealingAlgorithm;
import algorithms.simulatedannealing.model.ParametersSimulatedAnnealing;
import commons.TextFieldDouble;
import commons.TextFieldInteger;
import commons.UtilsController;
import input.DataReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

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
    }

}
