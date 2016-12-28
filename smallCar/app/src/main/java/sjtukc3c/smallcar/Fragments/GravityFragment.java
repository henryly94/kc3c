package sjtukc3c.smallcar.Fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import sjtukc3c.smallcar.Constants.MyConstants;
import sjtukc3c.smallcar.Modules.RemoteCommandManager;
import sjtukc3c.smallcar.Modules.SocketThreadMaster;
import sjtukc3c.smallcar.R;

import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;

/**
 * Author: wenhao.zhu[weehowe.z@gmail.com]
 * Created on 8:38 PM 12/27/16.
 */

public class GravityFragment extends MasterFragment {
    private static String TAG = GravityFragment.class.getSimpleName();

    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private SensorEventListener sensorEventListener;

    private static int mPort = 15536;
    private SocketThreadMaster mSocketThreadMaster;
    private RemoteCommandManager mCommandManager;

    private TextView mResutText;
    private TextView mResutText_Ins;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master_gravity, container, false);
        mResutText = (TextView) view.findViewById(R.id.gravity_text);
        mResutText_Ins = (TextView) view.findViewById(R.id.gravity_text_instruction);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (sensorManager == null) {
            mResutText.setText("SensorManager Unavailable");
            Log.e(TAG, "onCreateView: SensorManager Unavailable");
        }

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                mResutText.setText(String.valueOf(x) + "   " + String.valueOf(y) + "   " + String.valueOf(z));

                if ((y < -4) && (x > -2) && (x < 2)) {
                    mResutText_Ins.setText(MyConstants.INSTRUCTION_FORWARD);
                    mCommandManager.sendCommand(RemoteCommandManager.CMD_FOWARD);
                } else if ((y > 4) && (x > -2) && (x < 2)) {
                    mResutText_Ins.setText(MyConstants.INSTRUCTION_BACKWORD);
                    mCommandManager.sendCommand(RemoteCommandManager.CMD_BACK);
                } else if ((x > 4) && (y < 2) && (y > -2)) {
                    mResutText_Ins.setText(MyConstants.INSTRUCTION_LEFT);
                    mCommandManager.sendCommand(RemoteCommandManager.CMD_LEFT);
                } else if ((x < -4) && (y < 2) && (y > -2)) {
                    mResutText_Ins.setText(MyConstants.INSTRUCTION_RIGHT);
                    mCommandManager.sendCommand(RemoteCommandManager.CMD_RIGHT);
                } else {
                    mResutText_Ins.setText(MyConstants.INSTRUCTION_STOP);
                    mCommandManager.sendCommand(RemoteCommandManager.CMD_STOP);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(sensorEventListener, gravitySensor, SENSOR_DELAY_NORMAL);

        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroy();
        sensorManager.unregisterListener(sensorEventListener, gravitySensor);
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
