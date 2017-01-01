package sjtukc3c.smallcar.Modules;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import sjtukc3c.smallcar.Constants.MyConstants;

/**
 * Created by Administrator on 2016/12/21.
 */
public class BluetoothManager {

    private final static String DEBUG_TAG = "Lyy-Bluetooth-Manager";

    private Context mContext;

    private BluetoothAdapter mBluetoothAdapater;
    private BluetoothSocket mSocket;
    private BluetoothDevice mDevice;

    private UUID MyUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private boolean enabled = false;
    private boolean connected = false;
    private final static String mTargetName = "HC-05";
    private Handler mHandler;

    private Thread listener = new Thread(new Runnable() {
        @Override
        public void run() {

            while (true) {
                try {
                    Thread.sleep(100);
                    if (enabled) {
                        Message msg = new Message();
                        msg.what = MyConstants.TAG_BLUETOOTH_REQUEST;
                        mHandler.sendMessage(msg);
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                Log.e(DEBUG_TAG, device.getName() + ": " + device.getAddress());
                if (mTargetName.equals(device.getName())) {
                    Toast.makeText(mContext, String.format("Find target device: %s!", device.getName()), Toast.LENGTH_SHORT).show();
                    Log.e(DEBUG_TAG, "Find Target Device!");
                    mDevice = mBluetoothAdapater.getRemoteDevice(device.getAddress());
                    mContext.unregisterReceiver(this);
                    enabled = true;
                    mBluetoothAdapater.cancelDiscovery();
                }
            }
        }
    };


    public BluetoothManager(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
        listener.start();
        mBluetoothAdapater = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapater != null) {

            // 不许点"否"
            while (!mBluetoothAdapater.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                mContext.startActivity(enableIntent);
            }
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            mContext.registerReceiver(mReceiver, filter);
            mBluetoothAdapater.startDiscovery();
        } else {
            Log.e("Lyy", "xue血崩beng, meiyou lanya");
        }
    }


    public void buildUp() {
        ConnectThread th = new ConnectThread(mDevice);
        th.start();
    }


    public void sendMessage(String cmd) {
        // TODO: 2016/12/22 此处connected值可能为false 
        Log.e("Lyy", "Bluetooth Sending: " + cmd);
        try {
            OutputStream out = mSocket.getOutputStream();
            out.write(cmd.getBytes());
        } catch (IOException e) {
            Log.e("Lyy", "Bluetooth Sending Error: " + e.getMessage());
            buildUp();
            e.printStackTrace();
        }
    }


    public void close() {
        if (mSocket != null && mSocket.isConnected()) {
            try {
                mSocket.close();
                connected = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        enabled = false;
    }


    private class ConnectThread extends Thread {
        public ConnectThread(BluetoothDevice device) {
            mDevice = device;
            mSocket = null;
            try {
                mSocket = device.createRfcommSocketToServiceRecord(MyUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            if (mBluetoothAdapater.isDiscovering()) {
                mBluetoothAdapater.cancelDiscovery();
            }
            try {
                mSocket.connect();
                enabled = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
