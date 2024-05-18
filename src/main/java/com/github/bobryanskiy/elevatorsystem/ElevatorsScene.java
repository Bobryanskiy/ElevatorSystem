package com.github.bobryanskiy.elevatorsystem;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ElevatorsScene {
    @FXML
    AnchorPane pane;

    public AnchorPane getPane() {
        return pane;
    }

    Rectangle[] rectanglesLeft = new Rectangle[ElevatorsThread.FLOOR_AMOUNT];
    Rectangle[] rectanglesRight = new Rectangle[ElevatorsThread.FLOOR_AMOUNT];
    TextArea[] text = new TextArea[ElevatorsThread.FLOOR_AMOUNT + 1];

    @FXML
    public void initialize() {
        for (int i = 0; i < ElevatorsThread.FLOOR_AMOUNT; ++i) {
            rectanglesLeft[i] = new Rectangle(300, 55 * i + 10, 30, 50);
            rectanglesLeft[i].setFill(Color.LIMEGREEN);
            rectanglesRight[i] = new Rectangle(400, 55 * i + 10, 30, 50);
            rectanglesRight[i].setFill(Color.LIMEGREEN);

            text[i] = new TextArea();
            text[i].setPrefSize(200, 50);
            text[i].setTranslateX(500);
            text[i].setTranslateY(55 * i + 10);
        }
        text[23] = new TextArea();
        text[23].setPrefSize(200, 50);
        text[23].setTranslateX(500);
        text[23].setTranslateY(55 * 23 + 10);
        pane.getChildren().addAll(rectanglesLeft);
        pane.getChildren().addAll(rectanglesRight);
        pane.getChildren().addAll(text);
    }
}
