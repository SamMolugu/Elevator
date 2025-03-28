package org.example.ElevatorSimulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    //Elevator variables - for elevator object (in the case of multiple elevators)
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    static int currentFloor = 5;
    static int nextFloor = currentFloor;
    static int capacity = 0;
    static int passangerCount = 0;

    static final double weightThreshhold = 0.5;
    static final int maxLoad = 2500;
    static final int minFloor = 0;
    static final int maxFloor = 9;
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    //In memory storage data-structures
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //To simulate input stream for program
    static int[][] input;
    //Initially empty, used to track passengers getting on and off elevator
    static ArrayList<Integer> destinations = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
    //order of calls - could be used for a freq list, depending on number of elevators available by an external elevator service
    //based on the time-stamp of calls coming in within a given range (set) of high traffic hours.
    static Queue<Integer> floorStops = new LinkedList<>();
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        //Customer request(s) - Input stream to simulate elevator calls
        //Each index represents a set number of people waiting for each floor, associated with their corresponding destination floor.
        input = new int[][]{{3, 4, 5, 7, 8}, {}, {}, {0, 4, 6}, {0, 5, 9}, {}, {0, 3, 9}, {}, {}, {0}};
        //Order of operations - Sequence the floor calls came in for the elevator
        floorStops.add(0);
        floorStops.add(3);
        floorStops.add(4);
        floorStops.add(6);
        floorStops.add(9);


        //Algorithm(Program) - How the elevator operates.
        //The movement of the elevator can be visualized as a doubly linked list
        //The length of the input stream dictates how many iterations there are
        //(Total movement count between each floor to satisfy all customer orders)
        //Worse case being O(n^(m-1))
        //n = total floors available
        //m being how destination floors each call order can have, excluding the current floor (minus 1)
        pickUpPassengers();
        checkRemainingPassengers();
        dropOffRemainingPassengers();
    }

    //Picks up passengers waiting on each floor, on the way to the next floor on the call order list
    /*Updates head count on the elevator based on two factors:
    1. How many people are getting on the elevator at this floor
    2. How many people are getting off the elevator on the same (current) floor
     */
    public static void pickUpPassengers() {
        //People on some floor (x), waiting to be picked up.
        //Where x is greater than 0, and less than how many floors there are (n).
        //FYI starting floor / entry level / ground / first floor / the lowest floor are all marked as 0.
        //Regardless of above, on, or below ground level. (ease of readability / convenience)
        while (!floorStops.isEmpty()) {
            //check if call or stop order list is empty or valid
            //empty = no more call or drops, invalid = elevator passed by here already
            if (input[currentFloor].length > 0 ) {
                //Floor call value of -1 for the first index indicates a 'tombstone'.
                //Tombstone floors can be skipped over and removed from call order queue (floor call list).
                //Delete from queue if floor was already stopped at on the way to the original floor call in the call order queue.
                if (input[currentFloor][0] == -1 && floorStops.peek() == currentFloor) {
                    floorStops.poll();
                } else {
                    //check if anyone is getting off at this floor, and update head count
                    if (destinations.get(currentFloor) > 0) {
                        passangerCount -= destinations.get(currentFloor);
                        destinations.set(currentFloor, 0);
                    }
                    //pick up passengers at this floor number, and update head count
                    for (int n : input[currentFloor]) {
                        destinations.set(n, destinations.get(n) + 1);
                        passangerCount++;
                    }
                    //Only tombstone (mark non-empty floor, first index with -1):
                    //If we passed by this floor already on the way to another floor.
                    input[currentFloor][0] = -1;
                }
            }
            //Update to the next node in list (floor in building)
            currentFloor = move();
        }
    }

    //How many people are left on the elevator that need to get dropped off
    public static void checkRemainingPassengers() {
        int mid = maxFloor / 2;
        //To minimize the moves (iterations) the elevator makes:
        //See which half of the building the elevator is within (top half or bottom)
        if (currentFloor >= mid) {
            //Then push the remaining stops, on that side (first) into the floor stops queue
            //This is done to preserve O(log(x)) time, for total calls made
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

    //Follows the same logic as pick up method
    //Minus redundant checks for valid floor, and array restructuring for call order list integrity.
    //Instead, just drop off remaining passengers according to the floor stop list order.
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

    //Un-implemented
    //Checks to see if elevator current capacity is at or over the threshold (percentage)
    //Lets the elevator know when to off-load passengers to preserve capacity limits
    //defined by max carry load (maxLoad) variable
    public static boolean checkCapacity(int capacity) {
        if (capacity <= maxLoad * weightThreshhold) {
            return true;
        }
        return false;
    }

    //Un-implemented
    //logic to find the closest floor with the most passengers to drop-off, first before arriving at next defined stop
    //Start with current pos (currentFloor), and using two pointer algorithm (left/down and right/up)
    //Check floors on both directions until we find the largest drop-off (destination) floor
    //On route with next point, or non-empty number if no stops in a given direction (up or down)
    public static void adjustCartSize() {
        //Logic
    }

    //updates the elevator position to the next floor in the floor stop order list (up or down)
    public static int move() {
        //Same as idle state
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