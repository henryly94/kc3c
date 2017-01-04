---
layout: page
title: 软件部分
permalink: /software/
---
---

* TOC
{:toc}

---

## 开发环境

- OS `Ubuntu 14.04`/ `Windows 8.0`

- IDE: `Android Studio 1.1`

---

## 软件介绍

### 软件结构

软件部分分为主从双机. 


`主机(Master)` 为控制和监控端,接收从机发送的实时视频并且通过各种方式对从机下达指令

`从机(Slave)` 为硬件接口端,直接与小车通过蓝牙通信,并且实时向主机传送视频并按照主机下达的指令控制小车.

### 代码结构

软件架构采用了MVP的设计模式. 其中主从双方有各自的通信管理机制. 
![MVP-Android](/img/MVP-Android.png)

<br/>

同时为了方便双线开发,我采取的方法是通过构造出一个通用的中间模块CommandManager来将UI层和通信层的数据传输之间的借口抽象出来. 我们两个人在开发不同的功能时,只需要继承对应的Fragment来实现不同模式的代码编写,同时不同的Fragment之间的代码修改又不会互相影响. 
![此处插入CommandManager代码示意图]()

<br/>

而所有的Fragment又统一调用相同的CommandManager来作为命令接口. 这样就使得不同模块之间的耦合度降到最低,加快了我们的开发效率.

### UI设计

<br/>

我们采用Android的Material Design的元素来进行UI设计. 为了迎合多种不同的操作方式以及保持双机间视频传输, 将画面放在主体部分的同时,在上方加入SlidingTab来进行控制模式的切换,操作方式符合直觉习惯并且十分方便.

![MasterActivity](/img/MasterActivity_Normal.jpg)

<br/>

![SlaveActivity](/img/SlaveActivity_Capturing.jpg)

## Android编程

### 开发环境示意图
//TODO

### 代码示例
//TODO

## Setup
//TODO


## Issue
//TODO
