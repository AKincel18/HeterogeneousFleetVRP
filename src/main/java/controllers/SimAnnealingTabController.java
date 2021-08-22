package controllers;

import algorithms.simulatedannealing.SimulatedAnnealingAlgorithm;
import algorithms.simulatedannealing.model.ParametersSimulatedAnnealing;
import commons.client.CustomAlert;
import commons.client.TextFieldDouble;
import commons.client.TextFieldInteger;
import commons.client.UtilsController;
import exceptions.FieldsNotValidException;
import exceptions.InputException;
import input.DataReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

import static constants.StringConst.PROBABILITY_CANNOT_BE_ZERO_CONTENT_ERROR;
import static constants.StringConst.PROBABILITY_CANNOT_BE_ZERO_HEADER_ERROR;

public class SimAnnealingTabController extends UtilsController implements Initializable {

    @FXML private TextFieldDouble probabilityField;
    @FXML private TextFieldInteger t0IterationNumberField;
    @FXML private TextFieldDouble coolingFactorField;
    @FXML private TextFieldInteger iterationNumberField;
    @FXML private Button startButton;
    @FXML private ProgressBar progressBar;
    @FXML private Label runningLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startButton.setOnAction(event -> onStartButtonClicked());
    }

    private void onStartButtonClicked() {
        try {
            validate();
            DataReader dataReader = getInputData();
            changeFieldsProperties(true);

            SimulatedAnnealingAlgorithm simAnnealingAlg = new SimulatedAnnealingAlgorithm(
                    dataReader.getCities(),
                    dataReader.getVehicles(),
                    dataReader.getDepot(),
                    new ParametersSimulatedAnnealing(
                            probabilityField.getValue(),
                            t0IterationNumberField.getValue(),
                            coolingFactorField.getValue(),
                            iterationNumberField.getValue()
                    )
            );

            startAlgorithm(simAnnealingAlg);
            setOnSucceeded();
        } catch (InputException e) {
            new CustomAlert(Alert.AlertType.ERROR, e.getHeaderError(), e.getContentError(), ButtonType.OK).show();
        }
    }

    private void validate() throws InputException {
        validateProbability();
        validateCommon();
    }

    private void validateProbability() throws InputException {
        if (probabilityField.getValue() == 0.0)
            throw new FieldsNotValidException(PROBABILITY_CANNOT_BE_ZERO_HEADER_ERROR, PROBABILITY_CANNOT_BE_ZERO_CONTENT_ERROR);
    }

    private void setOnSucceeded() {
        currentTask.setOnSucceeded(event -> {
            changeFieldsProperties(false);
            showConfirmMessage();
        });
    }

    private void changeFieldsProperties(boolean isEnabled) {
        startButton.setDisable(isEnabled);
        probabilityField.setDisable(isEnabled);
        t0IterationNumberField.setDisable(isEnabled);
        coolingFactorField.setDisable(isEnabled);
        iterationNumberField.setDisable(isEnabled);

        runningLabel.setVisible(isEnabled);
        progressBar.setVisible(isEnabled);
    }

}
