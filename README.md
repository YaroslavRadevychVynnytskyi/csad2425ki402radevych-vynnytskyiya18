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
2. Plug into computer your Arduino board and select a port in Arduino IDE.
