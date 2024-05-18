package com.github.bobryanskiy.elevatorsystem;

import java.util.ArrayList;

public class Elevator {
    private Directions movingDirection;

    public ArrayList<Integer> getDestinations() {
        return destinations;
    }

    public int currentFloor;

    public void setMovingDirection(Directions movingDirection) {
        this.movingDirection = movingDirection;
    }

    public Directions getMovingDirection() {
        return movingDirection;
    }

    public int destinationFloor;
    private final ArrayList<Integer> destinations = new ArrayList<>();

    public Elevator(Directions movingDirection, int currentFloor, int destinationFloor) {
        this.movingDirection = movingDirection;
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
    }

    public void addDestinationFloor(int destinationFloor) {
        destinations.add(destinationFloor);
        if (this.movingDirection == Directions.Up)
            this.destinationFloor = Math.max(this.destinationFloor, destinationFloor);
        else if (this.movingDirection == Directions.Down)
            this.destinationFloor = Math.min(this.destinationFloor, destinationFloor);
        else {
            this.destinationFloor = destinationFloor;
            if (destinationFloor > currentFloor)
                this.movingDirection = Directions.Up;
            else
                this.movingDirection = Directions.Down;
        }
    }
}