package com.github.bobryanskiy.elevatorsystem;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class ElevatorsThread implements Runnable {
    public Elevator first, second;

    enum Which {left, right, noOne}

    static final int FLOOR_AMOUNT = 23;

    static ArrayList<Integer>[] queue = new ArrayList[FLOOR_AMOUNT];

    static {
        for (int i = 0; i < FLOOR_AMOUNT; ++i) {
            queue[i] = new ArrayList<>();
        }
    }

    private void updateElevator(Elevator elevator) {
//        int id = 5;
//        Rectangle rec = (Rectangle) Main.aP.getChildren().get(id);
//        rec.setFill(Color.RED);
//        Main.aP.getChildren().remove(id);
//        Main.aP.getChildren().add(id, rec);
        for (int person : queue[elevator.currentFloor]) {
            if (elevator.getMovingDirection() == Directions.Stay) {
                if (elevator.currentFloor == 1) {
                    elevator.addDestinationFloor(person);
                } else if (person < elevator.currentFloor) elevator.addDestinationFloor(person);
            } else if (elevator.getMovingDirection() == Directions.Up) {
                if (elevator.getDestinations().contains(elevator.currentFloor)) {
                    elevator.getDestinations().removeIf(f -> f == elevator.currentFloor);
                    //подсаживаю
                    if (person > elevator.currentFloor) elevator.addDestinationFloor(person);
                }
            } else if (elevator.getMovingDirection() == Directions.Down) {
                if (elevator.getDestinations().contains(elevator.currentFloor)) {
                    elevator.getDestinations().removeIf(f -> f == elevator.currentFloor);
                }
                //специально останавливаюсь
                if (person < elevator.currentFloor) elevator.addDestinationFloor(person);
            }
        }

        if (elevator.destinationFloor > elevator.currentFloor) elevator.currentFloor++;
        else if (elevator.destinationFloor < elevator.currentFloor) elevator.currentFloor--;
        if (elevator.currentFloor == elevator.destinationFloor) {
            elevator.setMovingDirection(Directions.Stay);
        }
    }

    int lastFrom, lastTo;

    @Override
    public void run() {
        first = new Elevator(Directions.Stay, 1, 1);
        second = new Elevator(Directions.Stay, 1, 1);
        while (true) {
            int from = ParametersGeneratorThread.from - 1;
            int to = ParametersGeneratorThread.to - 1;
            System.out.println(from + " MEOW " + to);
            if (lastFrom != from || lastTo != to)
                queue[from].add(to);
            lastFrom = from;
            lastTo = to;

            Which elevator = getWhich(from);
            switch (elevator) {
                case left -> first.addDestinationFloor(to);
                case right -> second.addDestinationFloor(to);
            }
            System.out.println("[1] - " + first.currentFloor);
            System.out.println("[2] - " + second.currentFloor);
            updateElevator(first);
            updateElevator(second);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Which getWhich(int from) {
        int closest = Integer.MAX_VALUE;
        Which elevator = Which.noOne;
        if (first.getMovingDirection() == Directions.Stay || second.getMovingDirection() == Directions.Stay) {
            if (first.getMovingDirection() == Directions.Stay) {
                closest = first.currentFloor;
                elevator = Which.left;
            }
            if (second.getMovingDirection() == Directions.Stay) {
                if (closest > second.currentFloor) {
                    closest = second.currentFloor;
                    elevator = Which.right;
                }
            }
        } else if (first.getMovingDirection() == Directions.Down || second.getMovingDirection() == Directions.Down) {
            if (first.getMovingDirection() == Directions.Down && from < first.currentFloor) {
                if (closest > first.currentFloor) {
                    closest = first.currentFloor;
                    elevator = Which.left;
                }
            }
            if (second.getMovingDirection() == Directions.Down && from < first.currentFloor) {
                if (closest > second.currentFloor) {
                    closest = second.currentFloor;
                    elevator = Which.right;
                }
            }
        }
        return elevator;
    }
}
