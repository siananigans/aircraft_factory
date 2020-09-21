# aircraft_factory
## Java Project to simulate an aircraft factory using threading and Semaphores.

- Each thread of 10 interacts with the aircraft by adding 'parts' to assemble the aircraft.
- A queing system is used to queue the aircraft for assembly.
- Uses semaphores to restict the amount of threads running and also to control access to the shared 
'parts' inventory. 
- The inventory is represented as a live interface.  
- Parts are added randomly in intervals to the inventory. 
