---
layout: page
title: 硬件部分
permalink: /hardware/
---

---

<!-- 这是用来生成目录的 -->
* TOC
{:toc}

---

<!-- anchor -->

## 开发环境

<br/>

- MCU Type: `TI-MSP430G2`

- OS: `ArchLinux`

- IDE: `Energia 1.6`

---

## 硬件介绍

<br/>

![hardware](/img/hardware.jpg)

### 硬件设备

小车控制采用`MSP430G2553`单片机，拥有串口通信，三路PWM控制器，较为丰富的I/O接口，适编程模型简单，用于多种控制场合。小车电源有4节AAA电池提供，其中电机驱动电路直接由电源提供，小车控制器以及其他数字部分由DC-DC电压转换器提供，电压3.3V。电机驱动采用`L293D`芯片.`L293D`采用16引脚DIP封装，其内部集成了四个双极型半H-桥电路，通过合适的电路连接，实现一个双路H桥电机驱动，以实现小车的前进后退以及调速。

### 总体结构示意图

<br/>

![structure](/img/hardware-structure.png)

### 单片机示意图

<br/>

![launchpad]({{site.cdn.url}}/launchpad.jpg)

### 单片机接线示意图

<br/>

![circuit](/img/circuit.jpg)

### 电机驱动示意图

<br/>

![LM293]({{site.cdn.url}}/LM293.png)

---

## 单片机编程

单片机程序使用`Energia`软件编写。程序中主要包含两个函数，`setup()`函数编写了硬件初始化行为，我们将几个管脚定义为输入输出，用来控制小车；而`loop()`函数是程序运行中的循环，是该程序的主要部分。在我们的程序中`loop()`函数用来监听蓝牙发送过来的数据并执行相应的动作，一共有`前进`、`后退`、`左转`、`右转`、`停止`5个动作。举例若收到蓝牙模块传来的前进信息，则让小车左右轮一同前进，具体写向管脚的值与接线有关。

### 开发环境示意图

<br/>

![Energiacode](/img/energiacode.png)

### 代码示例

```c++
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
}

void goBackward(){
  digitalWrite(LEFT_AHEAD,LOW);
  digitalWrite(LEFT_BACK,HIGH);
  digitalWrite(RIGHT_AHEAD,LOW);
  digitalWrite(RIGHT_BACK,HIGH);
}

void stop(){
  digitalWrite(LEFT_AHEAD,LOW);
  digitalWrite(LEFT_BACK,LOW);
  digitalWrite(RIGHT_AHEAD,LOW);
  digitalWrite(RIGHT_BACK,LOW);
}

void turnLeft(){
  digitalWrite(LEFT_AHEAD,HIGH);
  digitalWrite(LEFT_BACK,LOW);
  digitalWrite(RIGHT_AHEAD,LOW);
  digitalWrite(RIGHT_BACK,LOW);
}

void turnRight(){
  digitalWrite(LEFT_AHEAD,LOW);
  digitalWrite(LEFT_BACK,LOW);
  digitalWrite(RIGHT_AHEAD,HIGH);
  digitalWrite(RIGHT_BACK,LOW);
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
```
