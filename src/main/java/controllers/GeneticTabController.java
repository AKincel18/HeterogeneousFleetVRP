package controllers;

import algorithms.genetic.GeneticAlgorithm;
import algorithms.genetic.model.ParametersGenetic;
import algorithms.genetic.model.SelectionMethods;
import commons.TextFieldDouble;
import commons.TextFieldInteger;
import commons.UtilsController;
import input.DataReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;


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


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        selectionsComboBox.getItems().addAll(SelectionMethods.values());
        selectivePressureField.setFieldRanges(1.0, 2.0);
        startButtonListener();
        selectionsComboBoxListener();
    }

    private void startButtonListener() {
        startButton.setOnAction(event -> startAlgorithm());
    }

    private void startAlgorithm() {
        DataReader dataReader = getInputData();
        new GeneticAlgorithm(dataReader.getCities(), dataReader.getVehicles(), dataReader.getDepot(),
                new ParametersGenetic(
                        populationSizeField.getValue(),
                        iterationNumberField.getValue(),
                        crossoverProbField.getValue(),
                        mutationProbField.getValue(),
                        repeatingCrossoverNumberField.getValue(),
                        selectionsComboBox.getValue(),
                        tournamentSizeField.getValue(),
                        selectivePressureField.getValue())
        ).start();
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



}
