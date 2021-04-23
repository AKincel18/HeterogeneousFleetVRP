package controllers;

import algorithms.localsearch.LocalSearchAlgorithm;
import algorithms.localsearch.model.LocalSearchMethod;
import commons.UtilsController;
import input.DataReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;


public class LocalSearchTabController extends UtilsController implements Initializable {

    @FXML ComboBox<LocalSearchMethod> methodsComboBox;
    @FXML private Button startButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        methodsComboBox.getItems().addAll(LocalSearchMethod.values());
        startButtonListener();
    }

    private void startButtonListener() {
        startButton.setOnAction(event -> startAlgorithm());
    }

    private void startAlgorithm() {
        DataReader dataReader = getInputData();
        new LocalSearchAlgorithm(
                dataReader.getCities(),
                dataReader.getVehicles(),
                dataReader.getDepot(),
                methodsComboBox.getValue()
        ).start();
    }

}
