package sjtukc3c.smallcar.Activities;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import sjtukc3c.smallcar.R;

/**
 * Created by Administrator on 2016/12/15.
 */
public class ConnectionActivity extends Activity implements View.OnClickListener{

    private TextView mTextView_myIp, mTextView_targetIp, mTextView_message;
    private Button mButton_conn, mButton_send_recv;
    private WifiManager mWifiManager;

    private String mTargetIp;

    private static int mPort = 15536;

    private ServerSocket mServerSocket;
    private Socket mSocket;


    private final static int MSG_FLAG_NEW_MSG = 1;
    private final static int MSG_FLAG_RESET = 2;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_FLAG_NEW_MSG:
                    Bundle b = msg.getData();
                    Log.e("LYY", "msg:" + b.toString());
                    mTextView_message.setText(b.getString("msg", "No valid value"));
                    break;
                case MSG_FLAG_RESET:
                    break;
                default:
                    break;
            }

        }
    };

    Thread mRecvThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.e("Lyy", "" + mServerSocket.isClosed());
            try{
                while(!mServerSocket.isClosed()){
                    Thread.sleep(1000);
                    Log.e("Lyy", "receiving");
                    Socket socket = mServerSocket.accept();
                    if (socket != null){
                        Log.e("Lyy", "New connection come from " + socket.getInetAddress().toString());
                        createNewThread(socket);
                    } else {
                        Log.e("Lyy", "No new socket");
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        initView();
        initSocket();

    }

    private void initView(){
        mTextView_myIp = (TextView)findViewById(R.id.conn_tv_ip_value);
        mTextView_targetIp = (TextView)findViewById(R.id.conn_tv_tar_ip_value);
        mTextView_message = (TextView)findViewById(R.id.conn_tv_message);

        mButton_conn = (Button)findViewById(R.id.conn_btn_conn);
        mButton_conn.setOnClickListener(this);
        mButton_send_recv = (Button)findViewById(R.id.conn_btn_message);
        mButton_send_recv.setOnClickListener(this);
    }


    private void createNewThread(final Socket socket){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String buf;
                    while (socket.isConnected()){
                        buf = br.readLine();
                        if (buf != null){
                            Message msg = new Message();
                            msg.what = MSG_FLAG_NEW_MSG;
                            Bundle bundle = new Bundle();
                            bundle.putString("msg", buf);
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    private void initSocket(){
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mTextView_myIp.setText(getWIFILocalIpAdress());
        try {
            mServerSocket = new ServerSocket(mPort);
            mRecvThread.start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private String getWIFILocalIpAdress() {

        //获取wifi服务
        //判断wifi是否开启
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = formatIpAddress(ipAddress);
        return ip;
    }
    private static String formatIpAddress(int ipAdress) {
        return (ipAdress & 0xFF ) + "." +
                ((ipAdress >> 8 ) & 0xFF) + "." +
                ((ipAdress >> 16 ) & 0xFF) + "." +
                ( ipAdress >> 24 & 0xFF) ;
    }

    private void buildConnection(){
        String targetIp = mTextView_targetIp.getText().toString();
        try {
            mSocket = new Socket(targetIp, 15536);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendMessage(){
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Log.e("Lyy", "mServerSocket" + !mServerSocket.isClosed());
        final String targetIp = mTextView_targetIp.getText().toString();
        final String buffer = mTextView_message.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("Lyy", "Sending");
                    mSocket = new Socket(targetIp, 15536);
                    Log.e("Lyy", "Connection: " + mSocket.isConnected());
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                    bw.write(buffer);
                    bw.flush();
                    mSocket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.conn_btn_conn:
                buildConnection();
                break;
            case R.id.conn_btn_message:
                sendMessage();
            default:
                break;
        }
    }
}
