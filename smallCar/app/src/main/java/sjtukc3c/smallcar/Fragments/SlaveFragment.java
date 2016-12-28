package sjtukc3c.smallcar.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.net.ServerSocket;

import butterknife.ButterKnife;
import sjtukc3c.smallcar.Modules.CommandManager;
import sjtukc3c.smallcar.Modules.SocketThreadMaster;
import sjtukc3c.smallcar.R;

/**
 * Created by Administrator on 2016/12/20.
 */
public class SlaveFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_POSITION = "position";
    private int position;

    private static int mPort = 15536;

    private ServerSocket mServerSocket;
    private SocketThreadMaster mSocketThreadMaster;
    private SurfaceView mSurfaceView;

    public static EditText et;

    private CommandManager mCommandManager;

    private FragmentListener mFragmentListener;

    public interface FragmentListener {
        void begin();

        void end();
    }

    public static SlaveFragment newInstance(int p) {
        SlaveFragment f = new SlaveFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, p);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);

        // null & -1 is for default config(target_host, target_port)
        mCommandManager = new CommandManager(null, -1);

    }

    //    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        switch (position) {
            case 0:
                rootView = inflater.inflate(R.layout.fragment_slave_capturing, container, false);

                Button bg = (Button) rootView.findViewById(R.id.slave_btn_begin);
                Button ed = (Button) rootView.findViewById(R.id.slave_btn_end);
                TextView tv1 = (TextView) rootView.findViewById(R.id.slave_etv_my_ip);
                et = (EditText) rootView.findViewById(R.id.slave_etv_tar_ip);

                tv1.setText(
                        getWIFILocalIpAdress((WifiManager) rootView.getContext().getSystemService(Context.WIFI_SERVICE))
                );

                bg.setOnClickListener(this);
                ed.setOnClickListener(this);


                break;
            case 1:
            default:
                rootView = inflater.inflate(R.layout.fragment_slave_about, container, false);
                break;
        }
        ButterKnife.bind(this, rootView);

        return rootView;

    }

    private String getWIFILocalIpAdress(WifiManager mWifiManager) {

        //获取wifi服务
        //判断wifi是否开启
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = formatIpAddress(ipAddress);
        return ip;
    }

    private static String formatIpAddress(int ipAdress) {
        return (ipAdress & 0xFF) + "." +
                ((ipAdress >> 8) & 0xFF) + "." +
                ((ipAdress >> 16) & 0xFF) + "." +
                (ipAdress >> 24 & 0xFF);
    }

    public EditText getEt() {
        if (et != null) {
            return et;
        } else {
            return null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FragmentListener) {
            mFragmentListener = (FragmentListener) activity;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slave_btn_begin:
                mFragmentListener.begin();
                break;
            case R.id.slave_btn_end:
                mFragmentListener.end();
                break;
            case R.id.btn_master_voice:
                break;
        }
    }


}
