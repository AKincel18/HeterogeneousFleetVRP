package controllers;

import algorithms.localsearch.LocalSearchAlgorithm;
import algorithms.localsearch.model.LocalSearchMethod;
import commons.CustomAlert;
import commons.UtilsController;
import exceptions.ComboBoxNotFilledException;
import exceptions.InputException;
import input.DataReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

import static constants.StringConst.CHOOSE_A_METHOD;
import static constants.StringConst.METHODS_NOT_FILLED_HEADER_ERROR;


public class LocalSearchTabController extends UtilsController implements Initializable {

    @FXML ComboBox<LocalSearchMethod> methodsComboBox;
    @FXML private Button startButton;
    @FXML private ProgressBar progressBar;
    @FXML private Label runningLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        methodsComboBox.getItems().addAll(LocalSearchMethod.values());
        startButton.setOnAction(event -> onStartButtonClicked());
    }

    private void onStartButtonClicked() {
        try {
            validate();
            DataReader dataReader = getInputData();
            changeFieldsProperties(true);

            LocalSearchAlgorithm localSearchAlgorithm = new LocalSearchAlgorithm(
                    dataReader.getCities(),
                    dataReader.getVehicles(),
                    dataReader.getDepot(),
                    methodsComboBox.getValue()
            );

            startAlgorithm(localSearchAlgorithm);
            setOnSucceeded();
        } catch (InputException e) {
            new CustomAlert(Alert.AlertType.ERROR, e.getHeaderError(), e.getContentError(), ButtonType.OK).show();
        }

    }

    private void validate() throws InputException {
        validateMethods();
        validateCommon();
    }

    private void validateMethods() throws InputException {
        if (methodsComboBox.getValue() == null) {
            throw new ComboBoxNotFilledException(METHODS_NOT_FILLED_HEADER_ERROR, CHOOSE_A_METHOD);
        }
    }

    private void setOnSucceeded() {
        currentTask.setOnSucceeded(event -> {
            changeFieldsProperties(false);
            showConfirmMessage();
        });
    }

    private void changeFieldsProperties(boolean isEnabled) {
        startButton.setDisable(isEnabled);
        methodsComboBox.setDisable(isEnabled);

        runningLabel.setVisible(isEnabled);
        progressBar.setVisible(isEnabled);
    }
}
