#! /bin/bash

set -e

echo "Starting CI process for Java and Arduino..."

ROOT_PROJECT_PATH="."
JAVA_PROJECT_PATH="./client/rock-paper-scissors-client"
ARDUINO_PROJECT_PATH="./server/rock-paper-scissors-server"
DEPLOY_DIR="/home/user/nulp/csad/csad2425ki402radevych-vynnytskyiya18/deploy"

mkdir -p $DEPLOY_DIR

echo "=== Client Application Build and Test ==="

cd $JAVA_PROJECT_PATH

echo "Compiling Client(Java) project..."
mvn clean compile -B

echo "Running tests..."
mvn test

TEST_RESULTS_DIR="target/surefire-reports"
echo "Copying test results to $DEPLOY_DIR/test-results..."
cp -r $TEST_RESULTS_DIR $DEPLOY_DIR/test-results

echo "Packaging JAR..."
mvn package

echo "Copying JAR artifact... to $DEPLOY_DIR..."
cp target/*.jar $DEPLOY_DIR

echo "Generating tests statistics..."
mvn jacoco:report

echo "Copying tests statistics... to $DEPLOY_DIR..."
cp target/site/jacoco/index.html $DEPLOY_DIR/test-results

cd ../..

echo "=== Doxygen documentation generation ==="
echo "Generating documentation..."
doxygen Doxyfile

echo "=== Server Application (Arduino) Build ==="

echo "Compiling Arduino project..."
arduino-cli compile --fqbn arduino:avr:uno $ARDUINO_PROJECT_PATH/rock-paper-scissors-server.ino

echo "Uploading program to the board..."
arduino-cli upload -p /dev/ttyUSB0 --fqbn arduino:avr:uno $ARDUINO_PROJECT_PATH/rock-paper-scissors-server.ino

echo "CI process completed successfully. Artifacts are located in the $DEPLOY_DIR directory."

echo "=== DONE ==="



