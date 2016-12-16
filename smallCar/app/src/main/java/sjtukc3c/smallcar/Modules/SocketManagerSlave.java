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
            new Thread() {
                @Override
                public void run() {
                    camera.startPreview();
                    camera.setPreviewCallback(new InputVideoStream(h));

                    mConnectionStatus = MyConstants.Connection_On;

                    isConnected = true;

                }
            }.run();
        }
    }

    private void initCamera() {
        if (!isPreview)
            camera = Camera.open();
        if (camera != null && !isPreview) {
            try {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setPreviewSize(640, 480);
                parameters.setPictureFormat(ImageFormat.NV21);
                parameters.setPictureSize(640, 480);
                camera.setParameters(parameters);
                camera.setDisplayOrientation(90);
//                camera.setPreviewDisplay(surfaceHolder);
            } catch (Exception e) {
                e.printStackTrace();
            }
            isPreview = true;
        }
    }

    public void endConnection(){
        if (isConnected) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            isConnected = false;
        }

    }

    public void bindSocketCameraMaster(){

    }

    private class InputVideoStream implements Camera.PreviewCallback{

        String ipname;

        InputVideoStream(String ip){
            ipname = ip;
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            long curTime = (new Date(System.currentTimeMillis())).getTime();
            if (curTime - lastTime < 1000 / 30)
                return;
            else
                lastTime = curTime;
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
                    Thread th = new ClientThread(outstream, ipname);
                    th.start();
                }
            } catch (Exception ex) {
                Log.e("Sys", "Error:" + ex.getMessage());
            }
        }
    }


    class ClientThread extends Thread {
        private byte byteBuffer[] = new byte[1024];
        private OutputStream outsocket;
        private ByteArrayOutputStream myoutputstream;
        private String ipname;

        public ClientThread(ByteArrayOutputStream myoutputstream, String ipname) {
            this.myoutputstream = myoutputstream;
            this.ipname = ipname;
            try {
                myoutputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                Socket tempSocket = new Socket(ipname, mPort);
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
