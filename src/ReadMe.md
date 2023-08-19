# Rredictions By Nitzan!
### Submitted by Nitzan Ainemer, 313289472

## Intro

Welcome to Predictions, an advanced simulation engine designed for researchers who want to model and simulate complex scenarios. This platform provides a powerful and customizable framework for creating diverse simulations by simply uploading an XML file that describes the world. Whether you're simulating ecological systems, social dynamics, or other phenomena, Predictions is the perfect tool to turn your research concepts into dynamic simulations.

![img.png](img.png)

## Design Overview
There are three modules implemented in this project:
1.	Engine Module – the “heart” of the project. The Engine runs all the logic of the program. From handling file and validating data to running the simulation.
2.	Console UI – the user interface of the program. It manages the interaction with the user, and displaying the simulation results.
3.	DTO – data transfer objects module, a module designed to be the “buffer” between the engine and the UI. Any data that the engine needs to provide for the user, it does that using the DTO, and the opposite as well.

## Engine Module
The Engine Has many roles and functions. In this section I will present the main components of the Engine module.
1.	File Handling:
            a. 	The engine gets a path to a file and uses JAXB to unmarshall it to JAXB classes.
            b.	After the unmarshalling, the engine runs a “static” validation system that validates the data loaded to the JAXB classes. If it fails – it throws Exceptions from the engine to the UI, using a class in the DTO called ErrorDTO.
            If the “static” validation succeeded, we move on.
            c.	Mapping – the Engine maps the JAXB classes to the domain classes (That will be presented in this file).
            d.	After mapping validation – the engine runs another validation, that requires the domain objects to be “alive”. For example – this is where the “byExpression” validation happens.
2.	WORLD:
      a.	The engine holds and manages all the domain classes of the “world”. Here Is a class diagram to represent the classes and relations to “World”:
