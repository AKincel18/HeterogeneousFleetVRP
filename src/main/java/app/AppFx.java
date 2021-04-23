package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import static constants.StringConst.APP_TITLE;
import static constants.StringConst.MainFXML;

public class AppFx extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(MainFXML));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle(APP_TITLE);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
