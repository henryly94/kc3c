package sjtukc3c.smallcar.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import sjtukc3c.smallcar.Modules.SocketManagerSlave;
import sjtukc3c.smallcar.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class SlaveActivity extends Activity implements View.OnClickListener{

    private SocketManagerSlave mSocketManagerSlave;

    private EditText mEditText;

    private int mPort = 15536;

    private Button mButtonBegin, mButtonEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slave);

        initView();

        initTool();
    }


    private void initView(){
        mButtonBegin = (Button)findViewById(R.id.slave_btn_begin);
        mButtonBegin.setOnClickListener(this);

        mButtonEnd = (Button)findViewById(R.id.slave_btn_end);
        mButtonEnd.setOnClickListener(this);

        mEditText = (EditText)findViewById(R.id.slave_etv);
    }

    private void initTool(){
        mSocketManagerSlave = new SocketManagerSlave(this);

    }



    private void doBegin(){
        mSocketManagerSlave.BuildUpConnection(mEditText.getText().toString(), mPort);
    }

    private void doEnd(){
        mSocketManagerSlave.endConnection();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.slave_btn_begin:
                doBegin();
                break;
            case R.id.slave_btn_end:
                doEnd();
                break;
            default:
                break;
        }
    }
}
