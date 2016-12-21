package sjtukc3c.smallcar.Modules;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import sjtukc3c.smallcar.Constants.MyConstants;

/**
 * Created by Administrator on 2016/12/15.
 */
public class SocketManagerSlave {

    private Context mContext;
    private Socket mSocket;
    private int mPort = 15536;
    private int mConnectionStatus;
    private long lastTime = 0;

    private Camera.Parameters mParameters;


    private boolean isPreview = false;
    private boolean isConnected = false;

    private Camera camera;
    public SocketManagerSlave(Context context) {
        mContext = context;
        mConnectionStatus = MyConstants.Connection_Off;
    }

    public void BuildUpConnection(String host, int port) {
        initCamera();
        if (!isConnected) {
            final String h = host;
            final int p = port;
            Log.e("Lyy", "Send to: " + h + "/" + p);
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    camera.startPreview();
                    camera.setPreviewCallback(new InputVideoStream(h, p));
                    mConnectionStatus = MyConstants.Connection_On;
                    isConnected = true;
                }
            });
            th.start();
        } else {
            Log.e("Lyy", "Already Connected");
        }
    }

    private void initCamera() {
        if (!isPreview) {
            while (true) {
                try {
                    camera = Camera.open();
                    Log.e("Lyy", "Camera Opened");
                    break;
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("Lyy", "Already in Preview!");
        }

        if (camera != null && !isPreview) {
            try {

                Camera.Parameters parameters = camera.getParameters();
                parameters.setPreviewSize(640, 480);
                Log.e("Lyy", "Something here");
                parameters.setPictureFormat(ImageFormat.JPEG);
                parameters.setPictureSize(640, 480);
                mParameters = parameters;
                camera.setParameters(parameters);
                camera.setDisplayOrientation(90);
                isPreview = true;
                Log.e("Lyy", "Camera Initialization Success!");
            } catch (Exception e) {
                Log.e("Lyy", "出大事啦 Wrong!" + e.getMessage());

                e.printStackTrace();
            }

        }
    }

    public void endConnection(){
        if (isConnected) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            isConnected = false;
            isPreview = false;
        }

    }


    private class InputVideoStream implements Camera.PreviewCallback{

        String ipname;
        int port;
        InputVideoStream(String ip, int p){
            ipname = ip;
            port = p;
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            long curTime = (new Date(System.currentTimeMillis())).getTime();
            if (curTime - lastTime < 1000 / 10) {
                return;
            }
            else {
                lastTime = curTime;
            }
            Camera.Size size = camera.getParameters().getPreviewSize();
            try {
                YuvImage image = new YuvImage(data, ImageFormat.NV21,
                        size.width, size.height, null);
                if (image != null) {
                    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                    image.compressToJpeg(
                            new Rect(0, 0, size.width, size.height), 80,
                            outstream);
                    outstream.flush();
                    Thread th = new ClientThread(outstream, ipname, port);
                    th.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class ClientThread extends Thread {
        private byte byteBuffer[] = new byte[1024];
        private OutputStream outsocket;
        private ByteArrayOutputStream myoutputstream;
        private String ipname;
        private int port;

        public ClientThread(ByteArrayOutputStream myoutputstream, String ipname, int port) {
            this.myoutputstream = myoutputstream;
            this.ipname = ipname;
            this.port = port;
            try {
                myoutputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                Socket tempSocket = new Socket(ipname, port);
                if (tempSocket != null) {
                    outsocket = tempSocket.getOutputStream();
                    if (outsocket != null) {
                        ByteArrayInputStream inputstream = new ByteArrayInputStream(
                                myoutputstream.toByteArray());
                        int amount;
                        while ((amount = inputstream.read(byteBuffer)) != -1) {
                            outsocket.write(byteBuffer, 0, amount);
                        }
                        outsocket.close();
                    }
                    myoutputstream.flush();
                    myoutputstream.close();
                    tempSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
