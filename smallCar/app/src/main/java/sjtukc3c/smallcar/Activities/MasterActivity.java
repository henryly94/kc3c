package sjtukc3c.smallcar.Activities;

import android.app.Activity;
import android.os.Bundle;

import sjtukc3c.smallcar.Modules.SocketManager;
import sjtukc3c.smallcar.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class MasterActivity extends Activity {

    private SocketManager mSocketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        initView();
        initTool();
    }

    private void initView(){
        
    }

    private void initTool(){
        mSocketManager = new SocketManager(this);
    }

}
