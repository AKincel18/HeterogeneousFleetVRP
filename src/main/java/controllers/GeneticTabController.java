package controllers;

import algorithms.genetic.GeneticAlgorithm;
import algorithms.genetic.model.ParametersGenetic;
import algorithms.genetic.model.SelectionMethods;
import commons.CustomAlert;
import commons.TextFieldDouble;
import commons.TextFieldInteger;
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
import static constants.StringConst.SELECTIONS_NOT_FILLED_HEADER_ERROR;


public class GeneticTabController extends UtilsController implements Initializable {

    @FXML private ComboBox<SelectionMethods> selectionsComboBox;
    @FXML private Label tournamentSizeLabel;
    @FXML private Label selectivePressureLabel;
    @FXML private TextFieldInteger populationSizeField;
    @FXML private TextFieldInteger iterationNumberField;
    @FXML private TextFieldDouble crossoverProbField;
    @FXML private TextFieldDouble mutationProbField;
    @FXML private TextFieldInteger repeatingCrossoverNumberField;
    @FXML private TextFieldInteger tournamentSizeField;
    @FXML private TextFieldDouble selectivePressureField;
    @FXML private Button startButton;
    @FXML private ProgressBar progressBar;
    @FXML private Label runningLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        selectionsComboBox.getItems().addAll(SelectionMethods.values());
        startButton.setOnAction(event -> onStartButtonClicked());
        selectionsComboBoxListener();
    }

    private void onStartButtonClicked() {
        try {
            validate();
            DataReader dataReader = getInputData();
            changeFieldsProperties(true);

            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(dataReader.getCities(), dataReader.getVehicles(), dataReader.getDepot(),
                    new ParametersGenetic(
                            populationSizeField.getValue(),
                            iterationNumberField.getValue(),
                            crossoverProbField.getValue(),
                            mutationProbField.getValue(),
                            repeatingCrossoverNumberField.getValue(),
                            selectionsComboBox.getValue(),
                            tournamentSizeField.getValue(),
                            selectivePressureField.getValue())
            );

            startAlgorithm(geneticAlgorithm);
            setOnSucceeded();
        } catch (InputException e) {
            new CustomAlert(Alert.AlertType.ERROR, e.getHeaderError(), e.getContentError(), ButtonType.OK).show();
        }

    }

    private void validate() throws InputException {
        validateSelections();
        validateCommon();
    }

    private void validateSelections() throws InputException {
        if (selectionsComboBox.getValue() == null) {
            throw new ComboBoxNotFilledException(SELECTIONS_NOT_FILLED_HEADER_ERROR, CHOOSE_A_METHOD);
        }
    }

    private void selectionsComboBoxListener() {
        selectionsComboBox.setOnAction(event -> {
            switch (selectionsComboBox.getValue()) {
                case RANK:
                    setTournamentSizeVisibility(false);
                    setSelectivePressureVisibility(true);
                    break;
                case TOURNAMENT:
                    setTournamentSizeVisibility(true);
                    setSelectivePressureVisibility(false);
                    break;
                default:
                    setTournamentSizeVisibility(false);
                    setSelectivePressureVisibility(false);
            }
        });
    }

    private void setTournamentSizeVisibility(boolean enable) {
        tournamentSizeLabel.setVisible(enable);
        tournamentSizeField.setVisible(enable);
    }

    private void setSelectivePressureVisibility(boolean enable) {
        selectivePressureLabel.setVisible(enable);
        selectivePressureField.setVisible(enable);
    }

    private void setOnSucceeded() {
        currentTask.setOnSucceeded(event -> {
            changeFieldsProperties(false);
            showConfirmMessage();
        });
    }

    private void changeFieldsProperties(boolean isEnabled) {
        startButton.setDisable(isEnabled);
        populationSizeField.setDisable(isEnabled);
        iterationNumberField.setDisable(isEnabled);
        crossoverProbField.setDisable(isEnabled);
        mutationProbField.setDisable(isEnabled);
        repeatingCrossoverNumberField.setDisable(isEnabled);
        selectionsComboBox.setDisable(isEnabled);
        tournamentSizeField.setDisable(isEnabled);
        selectivePressureField.setDisable(isEnabled);

        progressBar.setVisible(isEnabled);
        runningLabel.setVisible(isEnabled);
    }

}
