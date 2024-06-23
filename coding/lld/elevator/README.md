## Elevator

Design a low-level architecture for an elevator system within a building. The system should handle multiple elevators, 
manage requests efficiently, and ensure safety and reliability.

### Requirements

- Users can request an elevator from any floor.
- Users can request to go to a specific floor once inside the elevator.
- Elevators should move up and down between floors.
- Elevators should stop at requested floors to pick up or drop off passengers.
- The system should manage multiple elevators.
- The system should assign the closest available elevator to a request.
- Elevators should not move if doors are open.
- Elevators should handle emergency stops.

### Entities
- Elevator
  - maxPassengers
  - passengers
  - status
  - currentFloor
- Building
  - elevators
- Passenger
  - ID
  - chosenFloor
  - currentFloor