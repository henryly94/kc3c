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

/**
 * Created by Administrator on 2016/12/17.
 */
public class SocketThreadMaster extends Thread{
    private ServerSocket serverSocket;
    private Canvas canvas;
    private Bitmap btp;
    private Matrix matrix;
    private SurfaceView surfaceView1;
    private SurfaceHolder surfaceHolder;
    private int mPort = 15536;
    private int mHeight, mWidth;
    public SocketThreadMaster(ServerSocket ss, SurfaceView sv, int port) {
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
            if (!serverSocket.isBound())
                serverSocket.bind(address, mPort);
        } catch (IOException e) {
            Log.e("Lyy", "Thread here 1");
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                if (serverSocket == null || serverSocket.isClosed()){
                    serverSocket = null;
                    serverSocket = new ServerSocket(mPort);
                }
                Socket s = serverSocket.accept();
                Log.e("Lyy", s.toString());
                if (s != null && mWidth > 0 && mHeight > 0) {
                    Log.e("Lyy", "S != null");
                    InputStream in = s.getInputStream();
                    btp = BitmapFactory.decodeStream(in);
                    Log.e("Lyy", btp.getWidth() + ", " + btp.getHeight());
                    in.close();
                    s.close();
                    btp = Bitmap.createBitmap(btp, 0, 0, btp.getWidth(),btp.getHeight(), matrix, true);
                    Log.e("Lyy", "My data: "+ mWidth + ", " + mHeight);
                    Bitmap newBtp = Bitmap.createScaledBitmap(btp, mWidth, mHeight, false);
                    Log.e("Lyy", newBtp.getWidth() + ", " + newBtp.getHeight());
                    canvas = surfaceHolder.lockCanvas();
                    canvas.drawBitmap(newBtp, 0, 0, null);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    btp.recycle();
                    newBtp.recycle();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
