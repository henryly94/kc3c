package sjtukc3c.smallcar.Modules;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.media.MediaRecorder;
import android.widget.Toast;

import sjtukc3c.smallcar.R;

/**
 * Created by Administrator on 2016/12/15.
 */
public class VideoCatcher {

    private Context mContext;

    private CameraManager mCameraManager;

    private MediaRecorder mMediaRecorder;


    public VideoCatcher(Context context) {
        mContext = context;
        if (!checkCameraHardware()) {
            Toast.makeText(mContext, mContext.getString(R.string.no_camera), Toast.LENGTH_SHORT).show();
            return;
        }
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    private boolean checkCameraHardware() {
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    public CameraManager getCameraManager() {
        return mCameraManager;
    }


}
