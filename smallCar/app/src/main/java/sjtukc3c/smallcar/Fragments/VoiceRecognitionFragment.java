package sjtukc3c.smallcar.Fragments;

import android.app.Fragment;

import sjtukc3c.smallcar.Modules.IVoiceDataPresenter;
import sjtukc3c.smallcar.Modules.IVoiceRecognition;

/**
 * Created by Administrator on 2016/12/16.
 */
public class VoiceRecognitionFragment extends Fragment {

    IVoiceRecognition mVoiceRecgnition;
    IVoiceDataPresenter mVoicePresenter;

    public void initialize(){

        mVoiceRecgnition.voice2String(mVoicePresenter
        );
    }
}
