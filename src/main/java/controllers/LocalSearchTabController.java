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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

import static constants.StringConst.CHOOSE_A_METHOD;
import static constants.StringConst.METHODS_NOT_FILLED_HEADER_ERROR;


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
        try {
            validate();
            DataReader dataReader = getInputData();
            new LocalSearchAlgorithm(
                    dataReader.getCities(),
                    dataReader.getVehicles(),
                    dataReader.getDepot(),
                    methodsComboBox.getValue()
            ).start();
        } catch (InputException e) {
            new CustomAlert(Alert.AlertType.ERROR, e.getHeaderError(), e.getContentError(), ButtonType.OK).show();
        }

    }

    private void validate() throws InputException {
        validateMethods();
        validateInput();
    }

    private void validateMethods() throws InputException {
        if (methodsComboBox.getValue() == null) {
            throw new ComboBoxNotFilledException(METHODS_NOT_FILLED_HEADER_ERROR, CHOOSE_A_METHOD);
        }
    }
}
