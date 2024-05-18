package com.github.bobryanskiy.elevatorsystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends javafx.application.Application {
    static ParametersGeneratorThread generator;
    static ElevatorsThread elevators;

    public static void main(String[] args) {
        generator = new ParametersGeneratorThread();
        Thread generatorThread = new Thread(generator);
        generatorThread.setDaemon(true);
        generatorThread.start();

        elevators = new ElevatorsThread();
        Thread elevatorsThread = new Thread(elevators);
        elevatorsThread.setDaemon(true);
        elevatorsThread.start();

        launch();
    }

    public static AnchorPane aP;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("elevators-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 1300);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        aP = (AnchorPane) stage.getScene().getRoot();
    }
}