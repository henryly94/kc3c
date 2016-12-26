package sjtukc3c.smallcar.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import sjtukc3c.smallcar.R;

/**
 * Created by Administrator on 2016/12/26.
 */
public class VoiceFragment extends MasterFragment{

    // TODO: 2016/12/26 一定要实现onCreateView 其他的函数可以按照需要自己定义 
    
    private TextView mResultText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        rootView = inflater.inflate(R.layout.fragment_master_voice, container, false);
        mResultText = (TextView) rootView.findViewById(R.id.voice_text);
        com.gc.materialdesign.views.Button voice = (com.gc.materialdesign.views.Button) rootView.findViewById(R.id.btn_master_voice);
        voice.setOnClickListener(this);
        voice.setOnTouchListener(this);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
