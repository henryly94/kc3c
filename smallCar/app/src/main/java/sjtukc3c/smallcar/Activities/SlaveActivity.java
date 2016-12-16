package sjtukc3c.smallcar.Activities;

import android.app.Activity;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import sjtukc3c.smallcar.Modules.Preview;
import sjtukc3c.smallcar.Modules.SocketManager;
import sjtukc3c.smallcar.Modules.VideoCatcher;
import sjtukc3c.smallcar.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class SlaveActivity extends Activity implements View.OnClickListener{

    private SocketManager mSocketManager;
    private MediaRecorder mMediaRecorder;
    private VideoCatcher mVideoCatcher;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;
    private TextureView mTextureView;

    private Button mButtonBegin, mButtonEnd;

    private Preview mPreview;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slave);

        initView();

        initTool();
    }


    private void initView(){
        mButtonBegin = (Button)findViewById(R.id.slave_btn_begin);
        mButtonBegin.setOnClickListener(this);

        mButtonEnd = (Button)findViewById(R.id.slave_btn_end);
        mButtonEnd.setOnClickListener(this);

        mSurfaceView = (SurfaceView)findViewById(R.id.slave_surfaceView);
    }

    private void initTool(){
        mSocketManager = new SocketManager(this);
        mVideoCatcher = new VideoCatcher(this);
    }

    private boolean safeCameraOpen(int id) {
        boolean qOpened = false;

        try {
            releaseCameraAndPreview();
            mCamera = Camera.open(id);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }

    private void releaseCameraAndPreview() {
        mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void doBegin(){

    }

    private void doEnd(){

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.slave_btn_begin:
                doBegin();
                break;
            case R.id.slave_btn_end:
                doEnd();
                break;
            default:
                break;
        }
    }
}
