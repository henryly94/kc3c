package sjtukc3c.smallcar.Modules;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

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
    public SocketThreadMaster(ServerSocket ss, SurfaceView sv, int port) {
        mPort = port;
        surfaceView1 = sv;
        serverSocket = ss;
        matrix = new Matrix();
        matrix.reset();
        matrix.postRotate(90);
        ViewGroup.LayoutParams params = surfaceView1.getLayoutParams();
        params.width = 480;
        params.height = 640;
        surfaceView1.setLayoutParams(params);
        surfaceHolder = surfaceView1.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }

            public void surfaceCreated(SurfaceHolder holder) {
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                if (serverSocket != null)
                    try {
                        serverSocket.close();
                        serverSocket = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        });
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        try {
            serverSocket = new ServerSocket(mPort);
            SocketAddress address = null;
            if (!serverSocket.isBound())
                serverSocket.bind(address, mPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                Socket s = serverSocket.accept();
                Log.e("Lyy", s.toString());
                if (s != null) {
                    Log.e("Lyy", "S != null");
                    InputStream in = s.getInputStream();
                    btp = BitmapFactory.decodeStream(in);
                    in.close();
                    s.close();
                    btp = Bitmap.createBitmap(btp, 0, 0, btp.getWidth(),
                            btp.getHeight(), matrix, true);
                    canvas = surfaceHolder.lockCanvas();
                    canvas.drawBitmap(btp, 0, 0, null);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    btp.recycle();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
