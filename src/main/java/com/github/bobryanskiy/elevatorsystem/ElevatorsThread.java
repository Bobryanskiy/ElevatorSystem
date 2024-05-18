package com.github.bobryanskiy.elevatorsystem;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class ElevatorsThread implements Runnable {
    public static Elevator first;
    public static Elevator second;

    enum Which {left, right, noOne}

    static final int FLOOR_AMOUNT = 23;

    static ArrayList<Integer>[] queue = new ArrayList[FLOOR_AMOUNT];

    static {
        for (int i = 0; i < FLOOR_AMOUNT; ++i) {
            queue[i] = new ArrayList<>();
        }
    }


    static int previd1 = 0, previd2 = 0;

    static int id1 = 0, id2 = 0;

    public static synchronized void updateFloors(AnchorPane pane) {
        for (int i = 0; i < FLOOR_AMOUNT; ++i) {
            ((TextArea)(pane.getChildren().get(FLOOR_AMOUNT * 3 - i - 1))).setText(queue[i].toString());
        }
    }

    public synchronized static void updateElevators(AnchorPane pane) {
        Platform.runLater(() -> ElevatorsThread.updateFloors(pane));

        Rectangle rec = (Rectangle) pane.getChildren().get(FLOOR_AMOUNT - id1 - 1);
        rec.setFill(Color.RED);
        pane.getChildren().remove(FLOOR_AMOUNT - id1 - 1);
        pane.getChildren().add(FLOOR_AMOUNT - id1 - 1, rec);

        rec = (Rectangle) pane.getChildren().get(FLOOR_AMOUNT * 2 - id2 - 1);
        rec.setFill(Color.RED);
        pane.getChildren().remove(FLOOR_AMOUNT * 2 - id2 - 1);
        pane.getChildren().add(FLOOR_AMOUNT * 2 - id2 - 1, rec);

        rec = (Rectangle) pane.getChildren().get(FLOOR_AMOUNT - first.destinationFloor - 1);
        rec.setFill(Color.BLUE);
        pane.getChildren().remove(FLOOR_AMOUNT - first.destinationFloor - 1);
        pane.getChildren().add(FLOOR_AMOUNT - first.destinationFloor - 1, rec);

        rec = (Rectangle) pane.getChildren().get(FLOOR_AMOUNT * 2 - second.destinationFloor - 1);
        rec.setFill(Color.BLUE);
        pane.getChildren().remove(FLOOR_AMOUNT * 2 - second.destinationFloor - 1);
        pane.getChildren().add(FLOOR_AMOUNT * 2 - second.destinationFloor - 1, rec);

        rec = (Rectangle) pane.getChildren().get(FLOOR_AMOUNT - previd1 - 1);
        rec.setFill(Color.LIMEGREEN);
        pane.getChildren().remove(FLOOR_AMOUNT - previd1 - 1);
        pane.getChildren().add(FLOOR_AMOUNT - previd1 - 1, rec);

        rec = (Rectangle) pane.getChildren().get(FLOOR_AMOUNT * 2 - previd2 - 1);
        rec.setFill(Color.LIMEGREEN);
        pane.getChildren().remove(FLOOR_AMOUNT * 2 - previd2 - 1);
        pane.getChildren().add(FLOOR_AMOUNT * 2 - previd2 - 1, rec);

        Platform.runLater(() -> ElevatorsThread.updateElevators(pane));
    }

    private synchronized void updateElevator(Elevator elevator) {
        if (elevator.currentFloor == elevator.destinationFloor) {
            elevator.setMovingDirection(Directions.Stay);
        }
        ArrayList<Integer> temp = new ArrayList<>();
        for (int person : queue[elevator.currentFloor]) {
            if (elevator.getMovingDirection() == Directions.Stay) {
                if (elevator.currentFloor == 0) {
                    elevator.addDestinationFloor(person);
                    temp.add(person);
                } else if (person < elevator.currentFloor) elevator.addDestinationFloor(person);
            } else if (elevator.getMovingDirection() == Directions.Up) {
                if (elevator.getDestinations().contains(elevator.currentFloor)) {
                    elevator.getDestinations().removeIf(f -> f == elevator.currentFloor);
                    //подсаживаю
                    if (person > elevator.currentFloor) {
                        elevator.addDestinationFloor(person);
                        temp.add(person);
                    }
                }
            } else if (elevator.getMovingDirection() == Directions.Down) {
                if (elevator.getDestinations().contains(elevator.currentFloor)) {
                    elevator.getDestinations().removeIf(f -> f == elevator.currentFloor);
                }
                //специально останавливаюсь
                if (person < elevator.currentFloor) {
                    elevator.addDestinationFloor(person);
                    temp.add(person);
                }
            }
        }
        queue[elevator.currentFloor].removeAll(temp);

        if (elevator.destinationFloor > elevator.currentFloor) elevator.currentFloor++;
        else if (elevator.destinationFloor < elevator.currentFloor) elevator.currentFloor--;
    }

    static int lastFrom, lastTo;

    @Override
    public void run() {
        first = new Elevator(Directions.Stay, 0, 0);
        second = new Elevator(Directions.Stay, 0, 0);
        while (true) {
            int from = ParametersGeneratorThread.from - 1;
            int to = ParametersGeneratorThread.to - 1;
            System.out.println(from + " MEOW " + to);
            if (lastFrom != from || lastTo != to) {
                queue[from].add(to);
                lastFrom = from;
                lastTo = to;

                Which elevator = getWhich(from, to);
                switch (elevator) {
                    case left -> first.addDestinationFloor(from);
                    case right -> second.addDestinationFloor(from);
                }
                System.out.println("[1] - " + first.currentFloor + " - " + first.destinationFloor);
                System.out.println("[2] - " + second.currentFloor + " - " + second.destinationFloor);

                synchronized (this) {
                    previd1 = id1;
                    previd2 = id2;
                    id1 = first.currentFloor;
                    id2 = second.currentFloor;
                }
            }
            updateElevator(first);
            updateElevator(second);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Which getWhich(int from, int to) {
        int closest = Integer.MAX_VALUE;
        Which elevator = Which.noOne;
        if (first.getMovingDirection() == Directions.Stay || second.getMovingDirection() == Directions.Stay) {
            if (first.getMovingDirection() == Directions.Stay) {
                closest = first.currentFloor;
                elevator = Which.left;
            }
            if (second.getMovingDirection() == Directions.Stay) {
                if (closest > Math.abs(to - second.currentFloor)) {
                    closest = second.currentFloor;
                    elevator = Which.right;
                }
            }
        } else if (first.getMovingDirection() == Directions.Up || second.getMovingDirection() == Directions.Up) {
            if (first.getMovingDirection() == Directions.Up && from > first.currentFloor && from > first.destinationFloor) {
                if (closest > Math.abs(to - first.currentFloor)) {
                    closest = first.currentFloor;
                    elevator = Which.left;
                }
            }
            if (second.getMovingDirection() == Directions.Up && from > first.currentFloor && from > first.destinationFloor) {
                if (closest > Math.abs(to - second.currentFloor)) {
                    closest = second.currentFloor;
                    elevator = Which.right;
                }
            }
        }
        return elevator;
    }
}
