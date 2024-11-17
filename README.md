## Repository Details

This repository is for the **Computer Systems Automated Design** (CSAD) labs. The repository follows the naming convention `csad<YY1YY2><group><student's full name><student's number>`. In this case, the repository name would be structured as:

- **csad2425ki402radevych-vynnytskyiya18**

Main development branch: **develop**

## Task Details

1. Create a GitHub repository with the name `csad2425ki402radevychvynnytskyiya18`, where:
   - **YY1** is the start of the studying year: **24**
   - **YY2** is the end of the studying year: **25**
   - **Group** is the student's group identifier: **ki402**
   - **Full name** is the student's full name: **Radevych-Vynnytskyi Yaroslav**
   - **Student's number** is: **18**

2. Grant repository access to the author/lecturer.

3. Create a branch in the following format: `feature/develop/<task number>`.
   Example: `feature/develop/task1`.

4. This README file will contain:
   - Details about the repository and task.
   - Information about the student's task as per Table 1.
   - Technology, programming language, and hardware details for the next tasks.

5. Create a Git tag in the format: `<PROJECT_NAME>_<VERSION>_WW<YYWWD>`, where:
   - **YY**: Current year
   - **WW**: Work week number
   - **D**: Current day number of the week

6. Create a pull request titled `task1` and assign the lecturer as a reviewer.

7. After the reviewer approves, merge the pull request into the `develop` branch.

## Student Details and details from Table 1

**Student Number**: 18\
**Student Name**: Radevych-Vynnytskyi Yaroslav\
**Group**: KI-402
___
**Game** : rock paper scissors\
**Config format** : INI

## Technologies, Programming Language, and Hardware

For the upcoming tasks, the following technologies, languages, and hardware will be used:

- **Programming Language**: Java
- **Build Tool**: Maven
- **Hardware**: Arduino Uno
___

## Task 2 Instructions
**Description of the system**:
Client (console Java app) asks user to enter a message which is then sent to the server. The server (runs on Arduino) responds with modified message. Basically, it adds "Hello, Java. You've sent: " plus user's original message.

**Requirements**: 
***Client***:
* JDK 17 or higher
* Maven 3.6.3 or higher

***Server***:
* Arduino IDE

To build and run client (Java application) you should:
1. Fetch ```feature/develop/task2``` branch
2. Navigate to ```csad2425ki402radevych-vynnytskyiya18/client/rock-paper-scissors-client```
3. Build project by typing ```mvn clean package```
4. Run the application by ```mvn exec:java```

To build and run server (Arduino sketch) you have to:
1. Navigate to ```csad2425ki402radevych-vynnytskyiya18/server/rock-paper-scissors-server```
2. Open file ```rock-paper-scissors-server.ino``` with Arduino IDE
3. Plug into computer your Arduino board and select a port in Arduino IDE
4. Program board by pressing ```upload``` button
___

## Task 3 Instructions
**Description of the system**:
This task is a **Rock Paper Scissors game** implemented with ```JavaFX``` as a client interface and an Arduino server to handle game logic and determine the winner. The client sends game input to the Arduino using the ```jSerialComm``` library, allowing real-time communication and result processing.

### Game Modes
The game supports three modes:

1. Man vs. Man: Two players compete against each other.
2. Man vs. Computer: A player competes against a computer opponent.
3. Computer vs. Computer: The computer plays against itself for demonstration purposes.

### Technologies Used
* JavaFX: Used to create the graphical interface for the game.
* jSerialComm: Java library for serial communication with the Arduino.
* Arduino: Manages game logic, evaluates player moves, and determines the winner.
### How It Works
1. The JavaFX client provides a user interface to select Rock, Paper, or Scissors.
2. Once a choice is made, the application sends data to the Arduino over a serial connection via jSerialComm.
3. The Arduino processes the choice, executes game logic, and determines the winner.
4. The result (win/lose/draw) is sent back to the JavaFX client and displayed.
___

## Task 4
**Added Doxygen documentation**

This project now uses Doxygen to generate code documentation.


After running local ci script, generated documentation will be available in the ```/deploy/docs``` directory (or the path specified in OUTPUT_DIRECTORY). Open docs/html/index.html in a browser to view it.
___

## Task 5
**Added Automated Tests**

Implemented integration test using JUnit 5 testing framework. Updated a local Continuous Integration (CI) script to generate and copy test statistics.


After running local ci script, generated test statistics will be available in the ```/deploy/test-results``` directory (or the path specified in OUTPUT_DIRECTORY). Open ```index.html``` in a browser to view it.
