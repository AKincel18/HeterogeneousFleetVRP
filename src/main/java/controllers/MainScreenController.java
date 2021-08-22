package controllers;

import commons.client.PathsHolder;
import input.DataReader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static constants.StringConst.*;

public class MainScreenController implements Initializable {

    @FXML private Tab geneticTab;
    @FXML private Tab localSearchTab;
    @FXML private Tab simAnnealingTab;
    @FXML private Tab tabuTab;
    @FXML private ComboBox<String> sheetsComboBox;
    @FXML private Label chooseFileLabel;
    @FXML private Label saveLocationLabel;
    @FXML private TextField outputFileTextField;
    private PathsHolder holder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            AnchorPane geneticPane = FXMLLoader.load(getClass().getResource(GeneticFXML));
            geneticTab.setContent(geneticPane);

            AnchorPane localSearchPane = FXMLLoader.load(getClass().getResource(LocalSearchFXML));
            localSearchTab.setContent(localSearchPane);

            AnchorPane simAnnealingPane = FXMLLoader.load(getClass().getResource(SimAnnealingFXML));
            simAnnealingTab.setContent(simAnnealingPane);

            AnchorPane tabuPane = FXMLLoader.load(getClass().getResource(TabuFXML));
            tabuTab.setContent(tabuPane);

            holder = PathsHolder.getInstance();

            outputFileTextField.textProperty().addListener((observable, oldValue, newValue) -> holder.setOutputFile(newValue));

        } catch (IOException ignore) {}
    }

    public void openFileChooser(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File: xlsx", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(((Node) mouseEvent.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            String path = selectedFile.getAbsolutePath();
            chooseFileLabel.setText(path);
            chooseFileLabel.setStyle("-fx-border-color: green");
            chooseFileLabel.setTooltip(new Tooltip(path));
            holder.setInputPath(path);
            sheetsComboBox.getItems().addAll(new DataReader(path, null).getSheets());
            sheetsComboBox.setOnAction(event -> holder.setSheet(sheetsComboBox.getValue()));
            sheetsComboBox.setDisable(false);
        }
        else {
            sheetsComboBox.getItems().addAll(new ArrayList<>());
            sheetsComboBox.setDisable(true);
        }
    }

    public void openDirectoryChooser(MouseEvent mouseEvent) {

        DirectoryChooser directoryChooser = new DirectoryChooser();

        //Set extension filter for text files
        File selectedDirectoryFile = directoryChooser.showDialog(((Node)mouseEvent.getSource()).getScene().getWindow());
        if (selectedDirectoryFile != null) {
            String directory = selectedDirectoryFile.getAbsolutePath();
            saveLocationLabel.setText(directory);
            saveLocationLabel.setTooltip(new Tooltip(directory));
            holder.setOutputPath(directory);
        }
    }

}
