package org.example.ElevatorSimulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    //Elevator variables - for elevator object (in the case of multiple elevators)
    static int currentFloor = 5;
    static int nextFloor = currentFloor;
    static int capacity = 0;
    static int passangerCount = 0;

    static final double weightThreshhold = 0.5;
    static final int maxLoad = 2500;
    static final int minFloor = 0;
    static final int maxFloor = 9;

    //in memory storage
    static int[][] input;
    static ArrayList<Integer> destinations = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
    //order of calls - could be used for a freq list, depending on number of elevators available by an external elevator service
    //based on the time-stamp of calls coming in within a given range (set) of high traffic hours.
    static Queue<Integer> floorStops = new LinkedList<>();


    public static void main(String[] args) {
        //Customer request - Input stream to simulate elevator calls
        //Each index represents a set number of people waiting for each floor, associated with their corresponding destination floor.
        input = new int[][]{{3, 4, 5, 7, 8}, {}, {}, {0, 4, 6}, {0, 5, 9}, {}, {0, 3, 9}, {}, {}, {0}};
        //Order of operations - sequence the floor calls came in for the elevator
        floorStops.add(0);
        floorStops.add(3);
        floorStops.add(4);
        floorStops.add(6);
        floorStops.add(9);


        //algorithm(program)
        pickUpPassengers();
        checkRemainingPassengers();
        dropOffRemainingPassengers();
    }

    //Picks up passengers waiting on each floor, on the way to the next floor on the call list
    /*Updates head count on the elevator:
    1. How many people are getting on the elevator at this floor
    2. If anyone is also getting off, on the same floor.
     */
    public static void pickUpPassengers() {
        while (!floorStops.isEmpty()) {
            //check if floor is empty or valid
            if (input[currentFloor].length > 0 ) {
                if (input[currentFloor][0] == -1 && floorStops.peek() == currentFloor) {
                    floorStops.poll();
                } else {
                    //check if anyone is getting off at this floor
                    if (destinations.get(currentFloor) > 0) {
                        passangerCount -= destinations.get(currentFloor);
                        destinations.set(currentFloor, 0);
                    }
                    //pick up passengers at this floor number
                    for (int n : input[currentFloor]) {
                        destinations.set(n, destinations.get(n) + 1);
                        passangerCount++;
                    }
                    input[currentFloor][0] = -1;
                }
            }
            currentFloor = move();
        }
    }

    //Once we have as many people on board as we can without hitting a weight threshold
    public static void checkRemainingPassengers() {
        int mid = maxFloor / 2;
        if (currentFloor >= mid) {
            for (int i = currentFloor; i <= maxFloor; i++) {
                if (destinations.get(i) != 0) {
                    floorStops.offer(i);
                }
            }
            for (int j = currentFloor; j >= minFloor; j--) {
                if (destinations.get(j) != 0) {
                    floorStops.offer(j);
                }
            }
        }
        else {
            for (int j = currentFloor; j >= minFloor; j--) {
                if (destinations.get(j) != 0) {
                    floorStops.offer(j);
                }
            }
            for (int i = currentFloor; i <= maxFloor; i++) {
                if (destinations.get(i) != 0) {
                    floorStops.offer(i);
                }
            }
        }
    }

    public static void dropOffRemainingPassengers() {
        while (!floorStops.isEmpty()) {
            if (currentFloor == floorStops.peek()) {
                passangerCount -= destinations.get(currentFloor);
                destinations.set(currentFloor, 0);
                floorStops.poll();
            }
            currentFloor = move();
        }
    }

    //Checks to see if elevator current capacity is at or over the threshold (percentage)
    public static boolean checkCapacity(int capacity) {
        if (capacity <= maxLoad * weightThreshhold) {
            return true;
        }
        return false;
    }

    //logic to find the closest floor with the most passengers to drop-off
    //start with current pos (currentFloor), and using two pointer algorithm
    //check floors on both directions until min, and max level until we find the floor with mist passangers to drop-off
    public static void adjustCartSize() {
        //algo
    }

    //updates the elevator position to the next floor in the call order (up or down)
    public static int move() {
        if (floorStops.isEmpty()) { return currentFloor; }
        else {
            if (floorStops.peek() < currentFloor) {
                nextFloor--;
            }
            else if (floorStops.peek() > currentFloor) { nextFloor++; }
        }
        return nextFloor;
    }
}