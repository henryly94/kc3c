package sjtukc3c.smallcar.Modules;

import android.content.Context;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import sjtukc3c.smallcar.Constants.MyConstants;

/**
 * Created by Administrator on 2016/12/15.
 */
public class SocketManager {

    private Context mContext;
    private Socket mSocket;
    private int mConnectionStatus;


    public SocketManager(Context context) {
        mContext = context;
        mConnectionStatus = MyConstants.Connection_Off;
    }

    public void BuildUpConnection(String host, int port) {
        final String h = host;
        final int p = port;
        new Thread(){
            @Override
            public void run() {
                try {
                    mSocket = new Socket(h, p);
                    mConnectionStatus = MyConstants.Connection_On;
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.run();

    }

    public void bindSocketCameraMaster(){

    }

    public void bindSocketCameraSlave(){

    }

    public void setSocket(String host, int port){
        BuildUpConnection(host, port);
    }

    public Socket getSocket() {
        return mSocket;
    }

    public int checkStatus() {
        return mConnectionStatus;
    }
}
