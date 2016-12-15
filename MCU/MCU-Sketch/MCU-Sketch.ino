// Pin Mapping [Check Document]

#define STEER P2_0
#define LEFT_AHEAD P2_1
#define LEFT_BACK P2_2
#define RIGHT_AHEAD P2_3
#define RIGHT_BACK P2_4

// AHEAD: HIGH  BACK: LOW -> MOVE FORWARD
// AHEAD: LOW  BACK: HIGH -> MOVE BACKWARD
// STEER: Change speed by changing duty ratio

// Interface

void goAhead();
void goBackward();
void turnLeft();
void turnRight();
void stop();

// Implementation

void goAhead() {
  digitalWrite(LEFT_AHEAD,HIGH);
  digitalWrite(LEFT_BACK,LOW);
  digitalWrite(RIGHT_AHEAD,HIGH);
  digitalWrite(RIGHT_BACK,LOW);
  //change steer
}

void goBackward(){
  digitalWrite(LEFT_AHEAD,LOW);
  digitalWrite(LEFT_BACK,HIGH);
  digitalWrite(RIGHT_AHEAD,LOW);
  digitalWrite(RIGHT_BACK,HIGH);
  //change steer
}

void stop(){
  digitalWrite(LEFT_AHEAD,LOW);
  digitalWrite(LEFT_BACK,LOW);
  digitalWrite(RIGHT_AHEAD,LOW);
  digitalWrite(RIGHT_BACK,LOW);
  //change steer
}

void turnLeft(){
  digitalWrite(LEFT_AHEAD,HIGH);
  digitalWrite(LEFT_BACK,LOW);
  digitalWrite(RIGHT_AHEAD,LOW);
  digitalWrite(RIGHT_BACK,LOW);
  //change steer
}

void turnRight(){
  digitalWrite(LEFT_AHEAD,LOW);
  digitalWrite(LEFT_BACK,LOW);
  digitalWrite(RIGHT_AHEAD,HIGH);
  digitalWrite(RIGHT_BACK,LOW);
  //change steer
}

// Main

void setup() {
  // Open serial communications and wait for port to open:
  Serial.begin(9600);

  Serial.println("\n\nKC3C-MCU-Sketch");
  Serial.println("weehowe.z@gmail.com");
  Serial.println("henryly94@gmail.com");

  pinMode(LEFT_AHEAD,OUTPUT);
  pinMode(LEFT_BACK,OUTPUT);
  pinMode(RIGHT_AHEAD,OUTPUT);
  pinMode(RIGHT_BACK,OUTPUT);
  pinMode(STEER,OUTPUT);

  stop();
}

void loop() {

  while (Serial.available() > 0){
    // read the incoming byte:
    char incomingByte = Serial.read();
    Serial.println(incomingByte);

    if (incomingByte == 'A') goAhead();
    else if (incomingByte == 'B') goBackward();
    else if (incomingByte == 'L') turnLeft();
    else if (incomingByte == 'R') turnRight();
    else if (incomingByte == 'S') stop();

  }
}
