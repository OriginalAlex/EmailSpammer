package com.originalalex.github.visual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    /*
    Note:
    I have only developed this software as a fun project, however due to my nature I very much want to circumvent Google's spam detection. This feature is a WIP, which I will hopefully add to this repo in the coming days.
     */

    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Display.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setResizable(false);
        stage.setTitle("Gmail Spammer");
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

}
