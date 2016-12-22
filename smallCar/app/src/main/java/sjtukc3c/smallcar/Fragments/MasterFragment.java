package sjtukc3c.smallcar.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.net.ServerSocket;

import butterknife.ButterKnife;
import sjtukc3c.smallcar.Modules.IVoiceDataPresenter;
import sjtukc3c.smallcar.Modules.RemoteCommandManager;
import sjtukc3c.smallcar.Modules.SocketThreadMaster;
import sjtukc3c.smallcar.R;

/**
 * Created by Administrator on 2016/12/20.
 */
public class MasterFragment extends Fragment
        implements View.OnClickListener, View.OnTouchListener{
    private static final String ARG_POSITION = "position";
    private int position;

    private static int mPort = 15536;

    private ServerSocket mServerSocket;
    private SocketThreadMaster mSocketThreadMaster;
    private SurfaceView mSurfaceView;
    private RemoteCommandManager mCommandManager;

    private TextView mTextView;


    private IVoiceDataPresenter mVoiceDataPresenter;


    public static MasterFragment newInstance(int p){
        MasterFragment f = new MasterFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, p);
        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

//    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        switch (position) {
            case 0:
                rootView = inflater.inflate(R.layout.fragment_master_normal, container, false);

                Button fd = (Button)rootView.findViewById(R.id.btn_master_foward);
                Button bk = (Button)rootView.findViewById(R.id.btn_master_back);
                Button lt = (Button)rootView.findViewById(R.id.btn_master_left);
                Button rt = (Button)rootView.findViewById(R.id.btn_master_right);
                Button stop = (Button)rootView.findViewById(R.id.btn_master_stop);

                fd.setOnClickListener(this);
                bk.setOnClickListener(this);
                lt.setOnClickListener(this);
                rt.setOnClickListener(this);
                stop.setOnClickListener(this);


                break;
            case 1:
                rootView = inflater.inflate(R.layout.fragment_master_voice, container, false);
                com.gc.materialdesign.views.Button voice = (com.gc.materialdesign.views.Button)rootView.findViewById(R.id.btn_master_voice);
                voice.setOnClickListener(this);
                voice.setOnTouchListener(this);

                break;
            case 2:
            default:
                rootView = inflater.inflate(R.layout.fragment_test_3, container, false);
                break;

        }
        ButterKnife.bind(this, rootView);

        return rootView;

    }

    public void setRemoteCommandManager(RemoteCommandManager cmdmanager){
        mCommandManager = cmdmanager;
    }

    private void checkThread(){
        if (mSocketThreadMaster != null){
            mSocketThreadMaster.stop();
            mSocketThreadMaster = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_master_foward:
                mCommandManager.sendCommand(RemoteCommandManager.CMD_FOWARD);
                break;
            case R.id.btn_master_back:
                mCommandManager.sendCommand(RemoteCommandManager.CMD_BACK);
                break;
            case R.id.btn_master_left:
                mCommandManager.sendCommand(RemoteCommandManager.CMD_LEFT);
                break;
            case R.id.btn_master_right:
                mCommandManager.sendCommand(RemoteCommandManager.CMD_RIGHT);
                break;
            case R.id.btn_master_stop:
                mCommandManager.sendCommand(RemoteCommandManager.CMD_STOP);
            case R.id.btn_master_voice:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.btn_master_voice:
                if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE){

                }
                break;
            default:
                break;
        }
        return false;
    }
}
