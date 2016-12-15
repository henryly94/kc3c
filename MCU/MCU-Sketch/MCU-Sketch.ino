// Pin Mapping [Check Document]

#define RIGHT_ENABLE P2_0
#define LEFT_ENABLE P2_5
#define LEFT_AHEAD P2_1
#define LEFT_BACK P2_2
#define RIGHT_AHEAD P2_3
#define RIGHT_BACK P2_4

// AHEAD: HIGH  BACK: LOW -> MOVE FORWARD
// AHEAD: LOW  BACK: HIGH -> MOVE BACKWARD
// LEFT_ENABLE: Change speed by changing duty ratio

// Default Value
#define VALUE_A 170
#define VALUE_B 128

// Interface
// Precise direction control -> analogWrite()

void goAhead();
void goBackward();
void turnLeft();
void turnRight();
void park();

// Implementation

void goAhead() {
  digitalWrite(LEFT_AHEAD,HIGH);
  digitalWrite(LEFT_BACK,LOW);
  digitalWrite(RIGHT_AHEAD,HIGH);
  digitalWrite(RIGHT_BACK,LOW);
  // analogWrite(PIN, VALUE);
}

void goBackward(){
  digitalWrite(LEFT_AHEAD,LOW);
  digitalWrite(LEFT_BACK,HIGH);
  digitalWrite(RIGHT_AHEAD,LOW);
  digitalWrite(RIGHT_BACK,HIGH);
}

void park(){
  digitalWrite(LEFT_AHEAD,LOW);
  digitalWrite(LEFT_BACK,LOW);
  digitalWrite(RIGHT_AHEAD,LOW);
  digitalWrite(RIGHT_BACK,LOW);
}

void turnLeft(){
  digitalWrite(LEFT_AHEAD,LOW);
  digitalWrite(LEFT_BACK,LOW);
  digitalWrite(RIGHT_AHEAD,HIGH);
  digitalWrite(RIGHT_BACK,LOW);
}

void turnRight(){
  digitalWrite(LEFT_AHEAD,HIGH);
  digitalWrite(LEFT_BACK,LOW);
  digitalWrite(RIGHT_AHEAD,LOW);
  digitalWrite(RIGHT_BACK,LOW);
}

// Main

void setup() {
  // Open serial communications and wait for port to open:
  Serial.begin(9600);

  Serial.println("\n\nKC3C-MCU");
  Serial.println("weehowe.z@gmail.com");
  Serial.println("henryly94@gmail.com");

  pinMode(LEFT_AHEAD,OUTPUT);
  pinMode(LEFT_BACK,OUTPUT);
  pinMode(LEFT_ENABLE,OUTPUT);
  pinMode(RIGHT_AHEAD,OUTPUT);
  pinMode(RIGHT_BACK,OUTPUT);
  pinMode(RIGHT_ENABLE,OUTPUT);

  digitalWrite(LEFT_ENABLE,HIGH);
  digitalWrite(RIGHT_ENABLE,HIGH);

  park();
}

void loop() {

  while (Serial.available() > 0){
    // read the incoming byte:
    char incomingByte = Serial.read();
    Serial.println(incomingByte);

    if (incomingByte == 'A') {
      Serial.println("Go ahead");
      goAhead();
    }
    else if (incomingByte == 'B') {
      Serial.println("Go backward");
      goBackward();
    }
    else if (incomingByte == 'L') {
      Serial.println("Turn left");
      turnLeft();
    }
    else if (incomingByte == 'R') {
      Serial.println("Turn Right");
      turnRight();
    }
    else if (incomingByte == 'P') {
      Serial.println("Stop");
      park();
    }
  }
}
