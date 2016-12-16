package sjtukc3c.smallcar.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sjtukc3c.smallcar.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class MainActivity extends Activity implements View.OnClickListener{

    private Button btn1, btn2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Test
        startActivity(new Intent(MainActivity.this, ConnectionActivity.class));

        initView();
    }

    private void initView(){
        btn1 = (Button)findViewById(R.id.btn_master);
        btn2 = (Button)findViewById(R.id.btn_slave);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_master:
                Intent toMaster = new Intent();
                toMaster.setClass(MainActivity.this, MasterActivity.class);
                toMaster.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(toMaster);
                break;

            case R.id.btn_slave:
                Intent toSlave = new Intent();
                toSlave.setClass(MainActivity.this, SlaveActivity.class);
                toSlave.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(toSlave);
                break;

            default:
                break;
        }

    }


}
