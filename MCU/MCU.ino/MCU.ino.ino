#define ENABLE P2_0
#define RIGHT_BACK P2_1
#define RIGHT_AHEAD P2_2
#define LEFT_BACK P2_3
#define LEFT_AHEAD P2_4

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
  pinMode(ENABLE,OUTPUT);

  digitalWrite(ENABLE,HIGH);
  digitalWrite(LEFT_AHEAD,HIGH);
  digitalWrite(LEFT_BACK,LOW);
  digitalWrite(RIGHT_AHEAD,HIGH);
  digitalWrite(RIGHT_BACK,LOW);
}

void loop() {
  while(true);
}
