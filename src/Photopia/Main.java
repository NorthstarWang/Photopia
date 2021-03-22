/*
The layout of the UI is determined in window.fxml, the event handlers are in Controller.java
Github: https://github.com/NorthstarWang
 */

package Photopia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import Photopia.Controller;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));
        primaryStage.getIcons().add(
                new Image(
                        Main.class.getResourceAsStream("icon.png" )));
        primaryStage.setTitle("Photopia");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
