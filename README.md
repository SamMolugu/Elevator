Elevator Program 

Given a 2D array input sequence, for example:
[[3,4,5,7,8], [], [], [0,4,6], [0,5,9], [], [0,3,9], [], [], [0]]
    0          1   2     3        4      5     6     7   8    9

Based on the input sequence above we can see there are 10 floors, 0-9. (Zero-indexed array)
Floors 2, 3, 6, 8, and 9 are all empty.
And five passengers waiting on floor 1, three on four, three on five and seven, and one on the 10th floor. 

Each index (n), of the 2D array corresponds with an elevator floor.
The set of numbers (m) at each index represents a group of people waiting to board the elevator at that floor.

From this we can make a few simple assumptions:
1. Each person gets off on atleast one floor
2. Same person cannot get off on multiple floor, in the same elevator call. (Input sequence / call order list)
3. Multiple floor calls from the same floor are aggregated into a subset (sub-array at each index of the array list)
   (This is to maintain uniqueness per floor level with a given set of elevator calls, using the array index as floor level. 
    From this the elevator could choose which floor to respond to first, based on proximity and time-stamp.)

With this data, we can synthesize the following constraints: 
n number of floors 0...n with,
m number of destinations, excluding the current floor (n - 1)
The max number of floor changes we could make given a set number of elevator calls is donated by:
O(n^m-1)

The problem domain:
How can we reduce the total number of stops made by the elevator to decrease overall, 
pick-up and drop-off time for each elevator floor call.

Approach:


