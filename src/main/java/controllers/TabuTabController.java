package controllers;

import algorithms.tabusearch.TabuSearchAlgorithm;
import algorithms.tabusearch.model.ParametersTabuSearch;
import commons.client.CustomAlert;
import commons.client.TextFieldInteger;
import commons.client.UtilsController;
import exceptions.InputException;
import input.DataReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class TabuTabController extends UtilsController implements Initializable {

    @FXML private TextFieldInteger iterationNumberField;
    @FXML private TextFieldInteger tabuIterationNumberField;
    @FXML private Button startButton;
    @FXML private ProgressBar progressBar;
    @FXML private Label runningLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startButton.setOnAction(event -> onStartButtonClicked());
    }

    private void onStartButtonClicked() {
        try {
            validateCommon();
            DataReader dataReader = getInputData();
            changeFieldsProperties(true);

            TabuSearchAlgorithm tabuSearchAlgorithm = new TabuSearchAlgorithm(
                    dataReader.getCities(),
                    dataReader.getVehicles(),
                    dataReader.getDepot(),
                    new ParametersTabuSearch(
                            iterationNumberField.getValue(),
                            tabuIterationNumberField.getValue())
            );

            startAlgorithm(tabuSearchAlgorithm);
            setOnSucceededTask();
        } catch (InputException e) {
            new CustomAlert(Alert.AlertType.ERROR, e.getHeaderError(), e.getContentError(), ButtonType.OK).show();
        }
    }

    private void setOnSucceededTask() {
        currentTask.setOnSucceeded(event -> {
            changeFieldsProperties(false);
            showConfirmMessage();
        });
    }

    private void changeFieldsProperties(boolean isEnabled) {
        startButton.setDisable(isEnabled);
        iterationNumberField.setDisable(isEnabled);
        tabuIterationNumberField.setDisable(isEnabled);
        
        progressBar.setVisible(isEnabled);
        runningLabel.setVisible(isEnabled);
    }
}
