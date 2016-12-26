package sjtukc3c.smallcar.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.ButterKnife;
import sjtukc3c.smallcar.Modules.IVoiceDataPresenter;
import sjtukc3c.smallcar.Modules.RemoteCommandManager;
import sjtukc3c.smallcar.Modules.SocketThreadMaster;
import sjtukc3c.smallcar.R;
import sjtukc3c.smallcar.Utils.JsonParser;

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

//    private ServerSocket mServerSocket;
    private SocketThreadMaster mSocketThreadMaster;
//    private SurfaceView mSurfaceView;
    private RemoteCommandManager mCommandManager;

//    private IVoiceDataPresenter mVoiceDataPresenter;

    // Print Reslut
    private TextView mResultText;

    // [IFLYTEK] VOICE OBJECT
    private com.iflytek.cloud.SpeechRecognizer mIat;

    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    //ENGINE TYPE
    private String mEngineType = SpeechConstant.TYPE_CLOUD;


    public static MasterFragment newInstance(int p) {
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

        // 初始化识别对象
        mIat = com.iflytek.cloud.SpeechRecognizer.createRecognizer(MasterFragment.this.getActivity(), mInitListener);
    }

    //    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;

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

            case 1:
                rootView = inflater.inflate(R.layout.fragment_master_voice, container, false);
                mResultText = (TextView) rootView.findViewById(R.id.voice_text);
                com.gc.materialdesign.views.Button voice = (com.gc.materialdesign.views.Button) rootView.findViewById(R.id.btn_master_voice);
                voice.setOnClickListener(this);
                voice.setOnTouchListener(this);

                break;

            case 2:

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

    // used for callback, success or not
    int ret = 0;

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
            case R.id.btn_master_voice:

                mIatResults.clear();
                setParam();

                ret = mIat.startListening(mRecognizerListener);
                if (ret != ErrorCode.SUCCESS) {
                    Log.e(TAG, "Failed, error code is " + ret);
                }

                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.btn_master_voice:
                if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE) {

                }
                break;
            default:
                break;
        }
        return false;
    }

    //Initialize Listener
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.e(TAG, "初始化失败，错误码：" + code);
            }
        }
    };

    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            Log.d(TAG, "volume：" + volume);
            Log.d(TAG, "data length：" + data.length);
        }

        @Override
        public void onBeginOfSpeech() {
            // this means the recorder is ready
            Log.d(TAG, "Start talking!");
            mResultText.setText("Start talking!");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d(TAG, "Finish talking!");
            mResultText.setText("Finish talking!");
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean isLast) {
            Log.d(TAG, recognizerResult.getResultString());
//            mResultText.setText(recognizerResult.getResultString());

            if (isLast) {
                printResult(recognizerResult);
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.e(TAG, "Error: " + speechError.getPlainDescription(true));
            mResultText.setText("Error: " + speechError.getPlainDescription(true));
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle bundle) {
            // For asking help = =
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = bundle.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                Log.d(TAG, "session id =" + sid);
            }
        }
    };

    // Use default param setting
    public void setParam() {

        mIat.setParameter(SpeechConstant.PARAMS, null);
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // release the connection
        mIat.cancel();
        mIat.destroy();
    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseResult(results.getResultString());
        String sn = null;

        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuilder resultBuffer = new StringBuilder();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        mResultText.setText(resultBuffer.toString());
    }
}


