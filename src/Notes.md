## Notes taken during class:

### Design:
1. use Definition. 
2. use api and impl. in the api hold enums and interfaces, and abstract.
3. create generate value method that returns object:
```` java
Object generateValue ();
````
4. Value generator:
   1. class that implements the value og properties.
   2. ````java
      Public Class Fixed valueGenerator<T> implements ValueGenerator<T> {}
      ````
  3. class for random, with abstract (api) and impl for each type.
4. Try to use abstract classes.
5. 
### General notes:
1. use commit and git repository.

### Entity Instance Manager:

```java
Entity create(EntityDefinition entityDefinition);
```

### Rule
interface for Rule.
interface for Action: invoke

Action Interface > AbstractAction implement Action > IncreaseAction extends AbstractAction

### UI-Engine
1. The toString of the objects are for "me" the developer. don't rely on it to present in the UI.
2. The UI will decide how to display the data to the user.
3. DTO - object that represents the data that the engine sends to the UI. contains data and no logic. raw data.

#### DTO:
What data should be in the DTO?
* Decided by the "UI" - what the exercise asks us for.
* In command num. 4 the exercise describes what needed to be in the DTO.
* a DTO is a single-use object! everytime we want to send an object to the UI, we create a new DTO object.
* consider creating a separate module for the DTO, just make sure that the DTO doesn't know the Engine object.
* No interfaces needed for the DTO objects.

#### Commands in the program:
1. Load: Everything in the Engine.
2. Display Data: use the DTO to show in the UI.
3. Run Simulation: combination of the UI and the Engine:
   1) Call the command - UI
   2) send the environment data to the user (DTO) - Engine
   3) collect data from the user for the envProperties and send the data to the Engine (DTO) and start simulation - UI.
4. Display Simulation output - Both Engine and UI.
5. Only (or at least mostly) in the UI.

