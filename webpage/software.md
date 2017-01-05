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

同时为了方便双线开发,我采取的方法是通过构造出一个通用的中间模块`CommandManager`来将UI层和通信层的数据传输之间的借口抽象出来. 我们两个人在开发不同的功能时,只需要继承对应的`Fragment`来实现不同模式的代码编写,同时不同的`Fragment`之间的代码修改又不会互相影响.

```java
// CommandManager 代码示意图
public class CommandManager {

    public final static String CMD_FOWARD = "F";
    public final static String CMD_BACK = "B";
    public final static String CMD_LEFT = "L";
    public final static String CMD_RIGHT = "R";

    private final static int MAX_RECONNECT_AMOUNT = 10;
    private static int reconnect_count = 0;

    private static boolean mutex = false;

    private static String mSlaveHost = "192.168.31.248";
    private static int mSlavePort = 15536;

    private Socket mSocket;

    public CommandManager(String h, int p) {
        setSlaveHost(h);
        setSlavePort(p);
        buildUpConnection();
    }

    public void sendCommand(String cmd) {
        switch (cmd) {
            case CMD_FOWARD:
            case CMD_BACK:
            case CMD_LEFT:
            case CMD_RIGHT:
                Log.e("Lyy", "Command: " + cmd);
                sendCommandToSlave(cmd);
                break;
            default:
                break;
        }
    }

    private void buildUpConnection() {
        reconnect_count += 1;
        if (reconnect_count > MAX_RECONNECT_AMOUNT && !mutex) {
            mutex = true;
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                        reconnect_count = 0;
                        mutex = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            th.start();
            return;
        }
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(mSlaveHost, mSlavePort);
                    mSocket.sendUrgentData(0);
                    reconnect_count = 0;
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        });
        th.start();
    }

    private void closeConnection() {
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void sendCommandToSlave(final String cmd) {
        if (mSocket == null || !mSocket.isConnected()) {
            buildUpConnection();
        }
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(mSlaveHost, mSlavePort);
                    DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());
                    writer.writeChars(cmd);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();


    }

    public void setSlaveHost(String newHost) {
        if (newHost != null) {
            mSlaveHost = newHost;
        }
    }

    public void setSlavePort(int newPort) {
        if (newPort != -1) {
            mSlavePort = newPort;
        }
    }

    public void flush() {
        closeConnection();
        buildUpConnection();
    }
}
```

而所有的Fragment又统一调用相同的`CommandManager`来作为命令接口. 这样就使得不同模块之间的耦合度降到最低,加快了我们的开发效率.

### UI设计


我们采用Android的**Material Design**的元素来进行UI设计. 为了迎合多种不同的操作方式以及保持双机间视频传输, 将画面放在主体部分的同时,在上方加入`SlidingTab`来进行控制模式的切换,操作方式符合直觉习惯并且十分方便.

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
