package sjtukc3c.smallcar.Modules;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Administrator on 2016/12/20.
 */
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

    private void buildUpConnection(){
        reconnect_count += 1;
        if (reconnect_count > MAX_RECONNECT_AMOUNT && !mutex){
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
                try{
                    mSocket = new Socket(mSlaveHost, mSlavePort);
                    mSocket.sendUrgentData(0);
                    reconnect_count = 0;
                } catch (IOException e){

                    e.printStackTrace();
                }
            }
        });
        th.start();
    }

    private void closeConnection(){
        if (mSocket != null){
            try {
                mSocket.close();
            } catch (IOException e){
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
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        th.start();


    }

    public void setSlaveHost(String newHost){
        if (newHost != null) {
            mSlaveHost = newHost;
        }
    }

    public void setSlavePort(int newPort){
        if (newPort != -1) {
            mSlavePort = newPort;
        }
    }

    public void flush(){
        closeConnection();
        buildUpConnection();
    }
}
