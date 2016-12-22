package sjtukc3c.smallcar.Modules;

import android.content.Context;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import sjtukc3c.smallcar.Constants.MyConstants;

/**
 * Created by Administrator on 2016/12/21.
 */
public class RemoteCommandManager {

    private Context mContext;

    public final static String CMD_FOWARD = "A";
    public final static String CMD_BACK = "B";
    public final static String CMD_LEFT = "L";
    public final static String CMD_RIGHT = "R";
    public final static String CMD_STOP = "P";

    private boolean enabled = false;
    private boolean connected = false;

    private Socket mSocket;
    private String mTargetIp = MyConstants.VALUE_LOCAL_IP;
    private int mTargetPort = 15546;

    public RemoteCommandManager(Context context){
        mContext = context;

    }

    public void buildUpConnection(){
        Log.e("Lyy", "Building up Command Connection...");
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    mSocket = new Socket(mTargetIp, mTargetPort);
                    enabled = true;

                    if (mSocket.isConnected()){
                        connected = true;
                        Log.e("Lyy", "Set up Command Connection Success");
                    }
                } catch (UnknownHostException e) {
                    Log.e("Lyy", "UnknownHostExp: "+e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("Lyy", "IOExp: "+e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        th.start();
    }

    public boolean isConnected(){
        return connected;
    }

    public void sendCommand(String _cmd){
        final String cmd = _cmd;
        if (mSocket == null || mSocket.isClosed() || !mSocket.isConnected()) {
            buildUpConnection();
        } else {
            try {
                DataOutputStream out = new DataOutputStream(mSocket.getOutputStream());
                String realCmd = String.format(
                        "{\"cmd\":\"%s\",\"%s\":\"%s\"}",
                        MyConstants.VALUE_COMMAND,
                        MyConstants.TAG_INSTRUCTION,
                        cmd
                );
                Log.e("Lyy", "Sending cmd: " + realCmd);
                Log.e("Lyy", "Sending cmd: " + mSocket.getInetAddress().getHostAddress() + "/" + mSocket.getPort());
                out.writeUTF(realCmd);
                out.flush();
            } catch (IOException e) {
                buildUpConnection();
                Log.e("Lyy", e.getMessage());
                e.printStackTrace();
            }
        }

    }

    public void setParams(String ip, int port){
        mTargetIp = ip;
        mTargetPort = port;
    }
}
