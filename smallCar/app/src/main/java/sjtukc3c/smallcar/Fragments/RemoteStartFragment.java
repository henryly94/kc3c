package sjtukc3c.smallcar.Fragments;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import sjtukc3c.smallcar.R;

/**
 * Created by Administrator on 2016/12/20.
 */
public class RemoteStartFragment extends DialogFragment implements View.OnClickListener{
    public static RemoteStartFragment newInstance(){
        return new RemoteStartFragment();
    }

    FragmentListener mFragmentListener;
    private EditText et;
    private int mPort = 15536;
    public interface FragmentListener{
        void send();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getDialog() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_master_config, container, false);

        android.widget.Button btn =  (android.widget.Button)root.findViewById(R.id.btn_master_remote);
        btn.setOnClickListener(this);
        et = (EditText)root.findViewById(R.id.et_master_remote);
        return root;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart() {
        super.onStart();

        // change dialog width
        if (getDialog() != null) {
            int fullWidth;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                fullWidth = size.x;
            } else {
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                fullWidth = display.getWidth();
            }

            final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                    .getDisplayMetrics());
            int w = fullWidth - padding;
            int h = getDialog().getWindow().getAttributes().height;
            getDialog().getWindow().setLayout(w, h);
        }
    }


    private void sendRequest(){
        final String ip = et.getText().toString();
        final int port = mPort;
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Socket s = new Socket(ip, port);
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());
                    String json = String.format("{\"cmd\":\"Connect\",\"Ip\":\"%s\",\"Port\":%d}",
                            ip, mPort);
                    out.writeChars(
                            json
                    );
                    out.flush();
                    out.close();
                    s.close();
                } catch (IOException e){
                    e.printStackTrace();
                }


            }
        });
        th.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_master_remote:
                sendRequest();
                break;
            default:
                break;
        }

    }
}
