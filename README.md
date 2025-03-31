Elevator Program 

Given a 2D array input sequence, for example:
[[3,4,5,7,8], [], [], [0,4,6], [0,5,9], [], [0,3,9], [], [], [0]]
    0          1   2     3        4      5     6     7   8    9

Based on the input sequence above we can see there are 10 floors, 0-9. (Zero-indexed array)
Floors 2, 3, 6, 8, and 9 are all empty.
With five passengers waiting on floor 1, three on four, three on five and seven, and one on the 10th floor. 

Each index (n), of the 2D array corresponds with an elevator floor.
The set of numbers (m) at each index represents a group of people waiting to board the elevator at that floor.

From this we can make a few simple assumptions:
1.Elevator starting point can be from any level on the plane 0, to n 
    (n being the highest floor accessible by the elevator, or in the building.)
2. Each person gets off on atleast one floor
3. Same person cannot get off on multiple floor, in the same elevator call. (Input sequence / call order list)
4. Multiple floor calls from the same floor are aggregated into a subset (sub-array at each index of the array list)
   (This is to maintain uniqueness per floor level with a given set of elevator calls, using the array index as floor level. 
    From this the elevator could choose which floor to respond to first, based on proximity and time-stamp.)

With this data, we can synthesize the following constraints: 
n number of floors 0...n with,
m number of destinations, excluding the current floor (n - 1)
The max number of floor changes we could make given a set number of elevator calls is donated by:
O(n^m-1)

The problem domain:
Say we have a list of elevator floor 'calls'. For each given call 'x', with n number of possibilities.
n being the number of floors available. How do we determine the net best move possible 'N', to reduce the number of calls (stack size) to 0. In the quickest time possible, defined by the least number of moves 'm' made by the elevator.
Simply put, how can we reduce the total number of stops made by the elevator to decrease overall, 
pick-up and drop-off time for each elevator floor call.

Approach:
Since we can pick up multiple people at the same floor or drop off multiple people at the same corresponding floor.
The easiest way to track the movement of each passanger on board, is through a frequency list. (Destinations List)
And for the call order list / user request(s) / or floor call order, we can use a Queue. (Floor-stops Queue/HashSet)
(Or a sorted hash-set to maintain integrity of the call order(s).)

I went with a queue, since we know the 2D input array is already unique since floors positions are indexed by the array length.

Algorithm: 
Given a set of floor calls, and non-empty queue of call orders. 
The elevator does two things on each floor it stops at:
1. Are we picking anyone up?
    If so, ask if this is our target floor (head of Queue), or if we have people to pick up here and its on the way.
       Update head-count and progress to next floor on call order list
3. Are we dropping any one off?
    If so update head-count on elevator and progress to next floor on call order list

Functionality:     
In a nutshell the algorithm checks to see what floor the first call that was made on, and begins to navigate towards that call floor.
The elevator picks up any passengers for the current pass-by floor on the way to the target floor. In this process, these floors are then marked as 'tombstones' in the input array sequence. By setting up a flag value of '-1' for the first index in the subarray, we can effectivly 'skip' over these same floors on the second pass. At each stop that is a pick-up or a tombstone, we pop from the queue.
Once all the pick-ups are complete, we begin dropping off the passengers via proximity to cut down on repetitive floor passes.

Unimplemented features:
How do I secure the elevator
1. Hide the network the elevator is on (By making it undiscoverable)
    1. Create a new subnet VLAN on the router. (Configure VLAN, and assign a new subnet ID) 
        Using linux we can partition a new subnet by specefying a range for the IoT devices using:
          >ip addr add . . . 
    2. We can set up a network firewall, and block all incoming traffic outside the network (making the subnet invisible) 
2. Then we can encrypt the traffic from the elevator to the central processor for the elevator floor requests (call order(s) list) 
    1. UDP ports for camera, TCP for card reader or biometric scanner using SSL/TLS 
    2. To record the actal transactions from the elevator to the processing server, we can use spring security with OAUTH (protocol)         and mark each transaction into a ledger (external database device) 
3. Store the data (verify/hash the elevator calls)
    1. We could use ORM tool like Hibernate to create a user DAO, for example: 
        - UID (EID) - Privledge (permission/access) level
        - Access Type (indicate civilian or federal employee)
        - Most recent access timestamp - Ledger of most recent transactions (clock in/out point for security and auditing purposes)
    2. Each elevator call(s) themselves stored as a hash function to hide user identity , and stored with a timestamp in a key/value database like cassandra for fast verification
    3. Authenticaed with a central database (PostgreSQL) server locally, used by the elevator control system for frequent updates to access permissions list or user roles. 
       Accessed via HQL for query updates and new user provisioning, or assigning roles.
       
How do I test the elevator - TDD (realibility and accessibility features)
1. carry load and max weight  I can create an algorithm to drop off the next nearest passengers using a b-tree to stores drop-off points above and below and go based of proximity (distance) to elevator until its below max weight threshold again.
2. Define failure points
     For example power outage, and the elevator resets half way. 
     We can hash the current call order list, and buttons pressed within the elevator and store it locally on the processor (CMOS)
3. Using Mockito/Powermock framework (within Spring) we can define these test/edge cases prior to elevator development. To ensure functionality prior to developing a elevator system. Ensuring we have the resources equipped to handle the elevator calls, in sufficient time to ensure client satisfaction. 
