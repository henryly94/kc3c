package sjtukc3c.smallcar.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.ButterKnife;
import sjtukc3c.smallcar.Modules.RemoteCommandManager;
import sjtukc3c.smallcar.Modules.SocketThreadMaster;
import sjtukc3c.smallcar.R;

/**
 * Author: wenhao.zhu[weehowe.z@gmail.com]
 * Created on 8:36 PM 2016/12/17.
 */

public class MasterFragment extends Fragment
        implements View.OnClickListener, View.OnTouchListener {
    private static String TAG = MasterFragment.class.getSimpleName();

    private static final String ARG_POSITION = "position";
    private int position;

    private static int mPort = 15536;

    private SocketThreadMaster mSocketThreadMaster;
    private RemoteCommandManager mCommandManager;

    public static MasterFragment newInstance(int p) {

        MasterFragment fragment;

        //TODO: 有新的Fragment， 按照position，添加一个case， 新建一个对应fragment类传递给f，理解成注册新函数
        switch (p) {
            case 1:
                fragment = new VoiceFragment();
                break;
            case 2:
                fragment = new GestureFragment();
                break;
            case 3:
                fragment = new GravityFragment();
                break;
            default:
                fragment = new MasterFragment();
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, p);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;

        switch (position) {
            case 0:
                rootView = inflater.inflate(R.layout.fragment_master_normal, container, false);

                Button fd = (Button) rootView.findViewById(R.id.btn_master_foward);
                Button bk = (Button) rootView.findViewById(R.id.btn_master_back);
                Button lt = (Button) rootView.findViewById(R.id.btn_master_left);
                Button rt = (Button) rootView.findViewById(R.id.btn_master_right);
                Button stop = (Button) rootView.findViewById(R.id.btn_master_stop);

                fd.setOnClickListener(this);
                bk.setOnClickListener(this);
                lt.setOnClickListener(this);
                rt.setOnClickListener(this);
                stop.setOnClickListener(this);

                break;

            default:
                rootView = inflater.inflate(R.layout.fragment_slave_about, container, false);
//                rootView = inflater.inflate(R.layout.fragment_test_3, container, false);
                break;

        }
        ButterKnife.bind(this, rootView);

        return rootView;

    }

    public void setRemoteCommandManager(RemoteCommandManager cmdmanager) {
        mCommandManager = cmdmanager;
    }

    private void checkThread() {
        if (mSocketThreadMaster != null) {
            mSocketThreadMaster.stop();
            mSocketThreadMaster = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.btn_master_voice:
                if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE) {
                    // I cant get THIS event
                    Log.e(TAG, "BUTTON_RELEASE_EVENT");
                }
                break;
            default:
                break;
        }
        return false;
    }

}


