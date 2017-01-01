package sjtukc3c.smallcar.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import sjtukc3c.smallcar.Constants.MyConstants;
import sjtukc3c.smallcar.Modules.RemoteCommandManager;
import sjtukc3c.smallcar.Modules.SocketThreadMaster;
import sjtukc3c.smallcar.R;

/**
 * Author: wenhao.zhu[weehowe.z@gmail.com]
 * Created on 8:38 PM 12/27/16.
 */

public class GestureFragment extends MasterFragment {

    private static String TAG = GestureFragment.class.getSimpleName();

    private static int mPort = 15536;

    private SocketThreadMaster mSocketThreadMaster;
    private RemoteCommandManager mCommandManager;

    private int x1;
    private int x2;
    private int y1;
    private int y2;

    private TextView mResutText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "GestureSimpleFragment");

        View view = inflater.inflate(R.layout.fragment_master_gesture, container, false);
        mResutText = (TextView) view.findViewById(R.id.gesture_text);


        mResutText.setOnTouchListener(new OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent e) {

                mResutText.setText("touch");

                mResutText.setFocusable(true);
                mResutText.setFocusableInTouchMode(true);
                mResutText.requestFocus();

                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = (int) e.getX();
                        y1 = (int) e.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = (int) e.getX();
                        y2 = (int) e.getY();
                        if (y1 - y2 > 100) {
                            mResutText.setText(MyConstants.INSTRUCTION_FORWARD);
                            mCommandManager.sendCommand(RemoteCommandManager.CMD_FOWARD);
                            Log.i(TAG, "onTouch: forward");
                        } else if (y2 - y1 > 100) {
                            mResutText.setText(MyConstants.INSTRUCTION_BACKWORD);
                            mCommandManager.sendCommand(RemoteCommandManager.CMD_BACK);

                            Log.i(TAG, "onTouch: backward");
                        } else if (x1 - x2 > 100) {
                            mResutText.setText(MyConstants.INSTRUCTION_LEFT);
                            mCommandManager.sendCommand(RemoteCommandManager.CMD_LEFT);

                            Log.i(TAG, "onTouch: left");
                            if (mResutText.isFocused()) Log.e(TAG, "onTouch: is focused");
                        } else if (x2 - x1 > 100) {
                            mResutText.setText(MyConstants.INSTRUCTION_RIGHT);
                            mCommandManager.sendCommand(RemoteCommandManager.CMD_RIGHT);

                            Log.i(TAG, "onTouch: right");
                            if (mResutText.isFocused()) Log.e(TAG, "onTouch: is focused");
                        } else if (((x1 - x2) < 10 || (x2 - x1) < 10) && ((y1 - y2) < 10 || (y2 - y1) < 10)) {
                            mResutText.setText(MyConstants.INSTRUCTION_STOP);
//                            mCommandManager.sendCommand(RemoteCommandManager.CMD_STOP);

                            Log.i(TAG, "onTouch: stop");
                        }
                        break;
                }
                return true;
            }
        });


        ButterKnife.bind(this, view);
        return view;
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

}
