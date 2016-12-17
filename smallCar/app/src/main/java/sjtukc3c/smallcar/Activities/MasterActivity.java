package sjtukc3c.smallcar.Activities;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import java.net.ServerSocket;

import sjtukc3c.smallcar.Modules.SocketThreadMaster;
import sjtukc3c.smallcar.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class MasterActivity
        extends Activity
        implements View.OnScrollChangeListener, View.OnTouchListener, View.OnClickListener{

    private ImageView mBackButton;
    private int mScrollX, mScrollWid;
    private HorizontalScrollView mScrollView;
    private SurfaceView mSurfaceView;

    private int mPort = 15536;


    private ServerSocket mServerSocket;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        initView();
        initTool();
    }

    private void initView(){
        mScrollView = (HorizontalScrollView)findViewById(R.id.master_scroll);
        mScrollWid = mScrollView.getMeasuredWidth();
        mScrollView.setOnScrollChangeListener(this);
        mScrollView.setOnTouchListener(this);

        mBackButton = (ImageView)findViewById(R.id.master_back);
        mBackButton.setOnClickListener(this);

        mSurfaceView = (SurfaceView)findViewById(R.id.master_surfaceview);
    }

    private void initTool(){
        Thread t = new SocketThreadMaster(mServerSocket, mSurfaceView, mPort);
        t.start();
    }


    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        mScrollX = scrollX;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.master_scroll:
                if (event.getAction() == MotionEvent.ACTION_UP){
//                    Log.e("Lyy", "" + mScrollX);
//                    if (mScrollX % mScrollWid < 0.2 * mScrollWid && mScrollX / mScrollWid >= 0){
////                        mScrollX
//
//                    } else if (mScrollX % mScrollWid > 0.8 * mScrollWid);
//                    mScrollView.setScrollX(((mScrollX + 0.2 )/mScrollWid * mScrollWid ));
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.master_back:
                finish();
                break;
            default:
                break;
        }
    }
}
