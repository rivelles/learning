# Parking Lot LLD

## Requirements
Based on [this](https://www.naukri.com/code360/library/design-a-parking-lot-low-level-design) exercise.

- The parking lot system should be able to park cars, bikes, trucks and handicapped vehicles.
- The parking lot will have multiple floors.
- The parking spots can be big, medium or small, and there are specific ones for handicapped.
- Trucks can only park in big ones, cars in big and mediums, bikes in all of them.
- The system should be able to calculate the cost of parking for each type of vehicle.
- The system should be able to keep track of the time a vehicle is parked.
- The system should be able to remove a vehicle from a parking spot.
- The system should be able to check the availability of parking spots and handicapped spots on a specific floor.
- The system should be able to calculate the number of hours a vehicle has been parked.

## Entities
1. Parking Lot: name, floors
2. Parking Floor: number, spots
3. Parking Spot: size (inheritance?)
4. Vehicle: type (inheritance?), base price