package controllers;

import commons.ExampleInputName;
import commons.PathsHolder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static constants.StringConst.*;

public class MainScreenController implements Initializable {

    @FXML private Tab geneticTab;
    @FXML private Tab localSearchTab;
    @FXML private Tab simAnnealingTab;
    @FXML private Tab tabuTab;
    @FXML private ComboBox<ExampleInputName> examplesComboBox;
    @FXML private Label chooseFileLabel;
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

            examplesComboBox.getItems().addAll(ExampleInputName.values());
            examplesComboBox.setOnAction(event -> holder.setExampleInputName(examplesComboBox.getValue()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openFileChooser(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Files: xlsx, csv", "*.xlsx", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(((Node) mouseEvent.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            String path = selectedFile.getAbsolutePath();
            chooseFileLabel.setText(path);
            chooseFileLabel.setStyle("-fx-border-color: green");
            chooseFileLabel.setTooltip(new Tooltip(path));
            holder.setInputPath(path);
        }
    }

}
