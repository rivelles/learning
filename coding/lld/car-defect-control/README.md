# Car Defect System

Requirements powered by ChatGPT :D

Problem Statement:

Design a system to manage and control car defects. The system should enable users to report defects, assign 
responsibility for fixing them, track the status of defect resolutions, and generate reports on defect occurrence and 
resolution efficiency. The system must support different user roles, including operators, supervisors, and managers.

-------------------------

## Entities

- User
- Authorization (enum)
- Defect
  - Urgency
  - Description
  - Solution
  - Time reported
  - Time solved
  - Assigned operator
- System

## Use cases
- Operators can report defects
- Reports will be unassigned. 
- A supervisor can assign it to an operator
- A manager can generate reports with all defect occurrences and their resolution