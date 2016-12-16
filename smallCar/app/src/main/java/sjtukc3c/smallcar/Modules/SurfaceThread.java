package sjtukc3c.smallcar.Modules;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by Administrator on 2016/12/15.
 */


public class SurfaceThread extends Thread {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    private ImageReader mImageReader;
    private Matrix mMatrix;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private TextureView mTextureView;

    private Handler mHandler;
    private Size mPreviewSize;
    private CaptureRequest.Builder mPreviewBuilder;
    private CameraManager mCameraMaganer;
    private CameraDevice mCameraDevice;
    private CameraCharacteristics mCameraChar;
    private CameraCaptureSession mSession;
    private CameraDevice.StateCallback mCameraDeviceStaCallback = new CameraDevice.StateCallback(){
        @Override
        public void onOpened(CameraDevice camera) {
            try {
                startPreview(mCameraDevice);
            } catch (CameraAccessException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera) {

        }

        @Override
        public void onError(CameraDevice camera, int error) {

        }
    };

    private CameraCaptureSession.StateCallback mSessionStateCallback = new CameraCaptureSession.StateCallback() {

        @Override
        public void onConfigured(CameraCaptureSession session) {
            try {
                updatePreview(session);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {

        }
    };

    public SurfaceThread(CameraManager cm, CameraDevice cd, TextureView txv, Size s){
        mCameraMaganer = cm;
        mCameraDevice = cd;
        mTextureView = txv;
        txv.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                try{
                    mCameraChar = mCameraMaganer.getCameraCharacteristics("0");
                    StreamConfigurationMap map = mCameraChar.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    mPreviewSize = map.getOutputSizes(SurfaceTexture.class)[0];
                    mCameraMaganer.openCamera("0", mCameraDeviceStaCallback, mHandler);

                } catch (CameraAccessException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
        mImageReader = ImageReader.newInstance(
                s.getWidth(),
                s.getHeight(),
                ImageFormat.JPEG,
                60
        );

        mMatrix = new Matrix();
        mMatrix.reset();
        mMatrix.postRotate(90);
    }

    private void startPreview(CameraDevice device) throws CameraAccessException{
        SurfaceTexture texture = mTextureView.getSurfaceTexture();
        texture.setDefaultBufferSize(mTextureView.getWidth(), mTextureView.getHeight());
        Surface surface = new Surface(texture);
        try{
            mPreviewBuilder = device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (CameraAccessException e){
            e.printStackTrace();
        }
        mImageReader = ImageReader.newInstance(mTextureView.getWidth(), mTextureView.getHeight(), ImageFormat.JPEG, 2);
        mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mHandler);
        mPreviewBuilder.addTarget(surface);
        mPreviewBuilder.addTarget(mImageReader.getSurface());
        mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), mSessionStateCallback, mHandler);
    }

    private void updatePreview(CameraCaptureSession session) throws CameraAccessException {
        session.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler);
    }

    private ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {

        /**
         *  当有一张图片可用时会回调此方法，但有一点一定要注意：
         *  一定要调用 reader.acquireNextImage()和close()方法，否则画面就会卡住！！！！！我被这个坑坑了好久！！！
         *    很多人可能写Demo就在这里打一个Log，结果卡住了，或者方法不能一直被回调。
         **/
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image img = reader.acquireNextImage();
            /**
             *  因为Camera2并没有Camera1的Preview回调！！！所以该怎么能到预览图像的byte[]呢？就是在这里了！！！我找了好久的办法！！！
             **/
            ByteBuffer buffer = img.getPlanes()[0].getBuffer();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            img.close();
        }
    };


    @Override
    public void run() {
        try{
            while (true){
                // TODO: 2016/12/15 Add Image Source here

                mBitmap = Bitmap.createBitmap(
                        mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), mMatrix, true
                );
                mCanvas = mSurfaceHolder.lockCanvas();
                mCanvas.drawBitmap(mBitmap, 0, 0, null);
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                mBitmap.recycle();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        super.run();
    }



}
