# soen343_smartHome

## Description of the project

An application that allows homeowners to automate tasks and services through the help of connected software applications. Users may automate the heating, lighting, ventilation, and other systems around their homes using an Internet connection. 

### Objective

Learn how to work with a client in an Agile development team to accomplish changes.

### Core Features

- Performing several actions to activate home systems and other household utilities (doors, windows, lights).
- Ensure that the heating system of a home appropriately regulates the household temperature.
- Ensure that the home is protected from intruders or unwanted individuals.

## Team Members

- George Koutsaris @georgekouts108 (40086174)
- Lucas Blanchard @lucasblanchard14 (40060670) 
- Antoine Turcotte @AntoTurc (40060978)
- Zhong Hao Li @kevinlizh1992 (40094223)
- Gang Hyun Kim @Gahyki (40097242) 

## Technologies

- Java
- JavaFX
- Scene Builder

## Architecture
The project architecture is based on the MVC pattern.  

## Setup

Please follow these guidelines before running SmartHomeSimulator_Main: 

For IntelliJ IDEA:

1) After opening the project “SmartHomeSimulator_Main”, click on File -> Project Structure…
2) In “Project Settings” select “Libraries”
3) At the top of the window, select “+” and choose “Java”
4) Choose the directory “lib” within the in soen343_project/java/javaFX/javafx-sdk-15 
5) Click on “Apply” and then “OK”. Close the Project Structure window.
6) Click on the dropdown next to the “Build Project” hammer icon, and select “Edit Configurations”
7) In the “Configuration” tab, expand the text field “VM options:” and paste the following: --module-path “” --add-modules javafx.controls,javafx.fxml
   Within the quotations (keep the quotation marks), paste the path of the “lib” directory in soen343_project/java/javaFX/javafx-sdk-15 
   NOTE: make sure that you add “/lib” at the end of the path*
8) Select “Apply” and “OK”. The JavaFX project should now compile without error.
