package com.github.bobryanskiy.elevatorsystem;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ElevatorsScene {
    public AnchorPane getPane() {
        return pane;
    }

    @FXML
    AnchorPane pane;

    Rectangle[] rectanglesLeft = new Rectangle[ElevatorsThread.FLOOR_AMOUNT];
    Rectangle[] rectanglesRight = new Rectangle[ElevatorsThread.FLOOR_AMOUNT];

    @FXML
    public void initialize() {
        for (int i = 0; i < ElevatorsThread.FLOOR_AMOUNT; ++i) {
            rectanglesLeft[i] = new Rectangle(300, 55 * i + 10, 30, 50);
            rectanglesLeft[i].setFill(Color.LIMEGREEN);
            rectanglesRight[i] = new Rectangle(400, 55 * i + 10, 30, 50);
            rectanglesRight[i].setFill(Color.LIMEGREEN);
        }
        pane.getChildren().addAll(rectanglesLeft);
        pane.getChildren().addAll(rectanglesRight);
    }
}
