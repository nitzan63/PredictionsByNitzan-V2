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



