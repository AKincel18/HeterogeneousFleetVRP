package controllers;

import algorithms.tabusearch.TabuSearchAlgorithm;
import algorithms.tabusearch.model.ParametersTabuSearch;
import commons.TextFieldInteger;
import commons.UtilsController;
import input.DataReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

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
        DataReader dataReader = getInputData();
        new TabuSearchAlgorithm(
                dataReader.getCities(),
                dataReader.getVehicles(),
                dataReader.getDepot(),
                new ParametersTabuSearch(
                        iterationNumberField.getValue(),
                        tabuIterationNumberField.getValue())
        ).start();
    }


}
