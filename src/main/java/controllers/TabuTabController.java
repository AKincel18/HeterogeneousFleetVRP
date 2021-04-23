package controllers;

import algorithms.tabusearch.TabuSearchAlgorithm;
import algorithms.tabusearch.model.ParametersTabuSearch;
import commons.CustomAlert;
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

public class TabuTabController extends UtilsController implements Initializable {

    @FXML private TextFieldInteger iterationNumberField;
    @FXML private TextFieldInteger tabuIterationNumberField;
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
            new TabuSearchAlgorithm(
                    dataReader.getCities(),
                    dataReader.getVehicles(),
                    dataReader.getDepot(),
                    new ParametersTabuSearch(
                            iterationNumberField.getValue(),
                            tabuIterationNumberField.getValue())
            ).start();
        } catch (InputException e) {
            new CustomAlert(Alert.AlertType.ERROR, e.getHeaderError(), e.getContentError(), ButtonType.OK).show();
        }
    }
}
