package sjtukc3c.smallcar.Modules;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/17.
 */
public class SocketThreadMaster extends Thread {


    private ServerSocket serverSocket;
    private Canvas canvas;
    private Bitmap btp;
    private Matrix matrix;
    private SurfaceView surfaceView1;
    private SurfaceHolder surfaceHolder;
    private int mPort = 15536;
    private int mCmdPort = 15546;
    private int mHeight, mWidth;
    public static boolean running = true;
    private RemoteCommandManager mCommandManager;

    public SocketThreadMaster(ServerSocket ss, SurfaceView sv, int port, RemoteCommandManager cmdmanager) {
        mCommandManager = cmdmanager;
        mPort = port;
        surfaceView1 = sv;
        serverSocket = ss;
        matrix = new Matrix();
        matrix.reset();
        matrix.postRotate(90);

        ViewTreeObserver vto = surfaceView1.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                surfaceView1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mWidth = surfaceView1.getWidth();
                mHeight = surfaceView1.getHeight();

            }
        });

        surfaceHolder = surfaceView1.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }

            public void surfaceCreated(SurfaceHolder holder) {
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                        serverSocket = null;
                        surfaceHolder.removeCallback(this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        try {
            serverSocket = new ServerSocket(mPort);
            Log.e("Lyy", "Thread here 2");
            SocketAddress address = null;
            if (!serverSocket.isBound()) {
                Log.e("Lyy", "Bound to Any Port");
                serverSocket.bind(address, 0);
            }
        } catch (IOException e) {
            Log.e("Lyy", "Thread here 1");
            e.printStackTrace();
        }
    }

    public void stopit() {
        running = false;
    }


    public void run() {
        try {
            int curTime = (int) ((new Date(System.currentTimeMillis())).getTime() % Integer.MAX_VALUE);
            Log.e("Lyy", "Start Time: " + curTime);
            running = true;
            while (running) {
                if (serverSocket == null || serverSocket.isClosed()) {
                    serverSocket = null;
                    serverSocket = new ServerSocket(mPort);
                }
                Log.e("Lyy", "Maybe here");
                Socket s = serverSocket.accept();
                Log.e("Lyy", s.toString());
                if (s != null && mWidth > 0 && mHeight > 0) {
                    if (!mCommandManager.isConnected()) {
                        mCommandManager.setParams(
                                s.getInetAddress().getHostAddress(), mCmdPort
                        );
                        mCommandManager.buildUpConnection();
                    }
                    Log.e("Lyy", s.getInetAddress().getHostAddress() + " | " + s.getInetAddress().getHostName());
                    Log.e("Lyy", "S != null");
                    InputStream in = s.getInputStream();
                    byte[] newTimeBytes = new byte[16];
                    in.read(newTimeBytes, 0, 8);
                    in.read(newTimeBytes, 8, 8);
                    int newTime = Integer.parseInt(new String(newTimeBytes));
                    Log.e("Lyy", "New Time: " + newTime);
//                    in.skip(16);
                    if (newTime > curTime) {
                        curTime = newTime;
                        btp = BitmapFactory.decodeStream(in);
                        Log.e("Lyy", btp.getWidth() + ", " + btp.getHeight());
                        in.close();
                        s.close();
                        btp = Bitmap.createBitmap(btp, 0, 0, btp.getWidth(), btp.getHeight(), matrix, true);
                        Log.e("Lyy", "My data: " + mWidth + ", " + mHeight);
                        Bitmap newBtp = Bitmap.createScaledBitmap(btp, mWidth, mHeight, false);
                        Log.e("Lyy", newBtp.getWidth() + ", " + newBtp.getHeight());
                        canvas = surfaceHolder.lockCanvas();
                        canvas.drawBitmap(newBtp, 0, 0, null);
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        btp.recycle();
                        newBtp.recycle();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String formatIpAddress(int ipAdress) {
        return (ipAdress & 0xFF) + "." +
                ((ipAdress >> 8) & 0xFF) + "." +
                ((ipAdress >> 16) & 0xFF) + "." +
                (ipAdress >> 24 & 0xFF);
    }
}
