package sjtukc3c.smallcar.Activities;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import sjtukc3c.smallcar.Fragments.SlaveFragment;
import sjtukc3c.smallcar.Modules.SocketManagerSlave;
import sjtukc3c.smallcar.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class SlaveActivity extends AppCompatActivity
        implements View.OnClickListener, SlaveFragment.FragmentListener{

    private SocketManagerSlave mSocketManagerSlave;

    private ImageView mBackbtn;

    private SystemBarTintManager mTintManager;
    private Drawable oldBackground;
    private Toolbar mToolbar;

    private SlaveFragment mFragment;

    private ViewPager mPager;
    private PagerSlidingTabStrip mTab;
    private MyAdapter mAdapter;

    private static boolean mThreadEnd = false;

    private int mPort = 15536;

    private ServerSocket mServerSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slave);
        initView();
        initTool();
    }


    private void initView(){
        mBackbtn = (ImageView)findViewById(R.id.btn_slave_go_back);
        mBackbtn.setOnClickListener(this);




        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mPager = (ViewPager)findViewById(R.id.slave_pager);
        mTab = (PagerSlidingTabStrip)findViewById(R.id.slave_tabs);
        mAdapter =  new MyAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(0);
        mTab.setViewPager(mPager);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        mPager.setPageMargin(pageMargin);


        mTab.setOnTabReselectedListener(new PagerSlidingTabStrip.OnTabReselectedListener() {
            @Override
            public void onTabReselected(int i) {
                Toast.makeText(SlaveActivity.this, "Tab reselected: " + i, Toast.LENGTH_SHORT).show();
            }
        });

        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        changeColor(ContextCompat.getColor(getBaseContext(), R.color.green));
    }

    private void initTool(){
        mThreadEnd = false;
        mSocketManagerSlave = new SocketManagerSlave(this);
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                if (mServerSocket == null){
                    try {
                        mServerSocket = new ServerSocket(mPort);
                        while (!mThreadEnd){
                            Socket s = mServerSocket.accept();
                            if (s != null){
                                DataInputStream dis = new DataInputStream(s.getInputStream());
                                String json = dis.readUTF();
                                try {
                                    org.json.JSONObject newJson = new JSONObject(json);
                                    if (newJson.getString("cmd").equals("Connection")){
                                        String ip = newJson.getString("Ip");
                                        int port = newJson.getInt("Port");
                                        mPort = port;
                                        mFragment.getEt().setText(ip);
                                        doBegin();

                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }

                            }
                        }
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        th.start();
    }



    private void changeColor(int newColor) {
        mTintManager.setTintColor(newColor);
        // change ActionBar color just if an ActionBar is available
        Drawable colorDrawable = new ColorDrawable(newColor);
        Drawable bottomDrawable = new ColorDrawable(ContextCompat.getColor(getBaseContext(), android.R.color.transparent));
        LayerDrawable ld = new LayerDrawable(new Drawable[]{colorDrawable, bottomDrawable});

        if (oldBackground == null) {
            getSupportActionBar().setBackgroundDrawable(ld);
        } else {
            TransitionDrawable td = new TransitionDrawable(new Drawable[]{oldBackground, ld});
            getSupportActionBar().setBackgroundDrawable(td);
            td.startTransition(200);
        }

        oldBackground = ld;
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof SlaveFragment){
            mFragment = (SlaveFragment)fragment;
        }
    }

    private void doBegin(){
        Log.e("Lyy", "mSocket " + (mSocketManagerSlave == null));
        Log.e("Lyy", "mFragment " + (mFragment == null));
        if (mFragment == null){
            mFragment = (SlaveFragment)mAdapter.getFragment();
        }
        Log.e("Lyy", "After mFragment " + (mFragment == null));
        mSocketManagerSlave.BuildUpConnection(mFragment.et.getText().toString(), mPort);
    }

    private void doEnd(){
        mSocketManagerSlave.endConnection();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_slave_go_back:
                finish();
            default:
                break;
        }
    }

    @Override
    public void begin() {
        doBegin();
    }

    @Override
    public void end() {
        doEnd();
    }

    private class MyAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Capturing", "About"};

        private SparseArray<Fragment> fragments;

        public int curPos = 0;

        public MyAdapter(FragmentManager fm) {
            super(fm);
            fragments = new SparseArray<>(getCount());
            Log.e("Lyy", "SparseArray " + getCount());
        }

        @Override
        public Fragment getItem(int position) {
            SlaveFragment sf = SlaveFragment.newInstance(position);
            fragments.put(position, sf);
            curPos = position;
            return sf;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.e("Lyy", "getPageTitle: " + TITLES[position]);
            return TITLES[position] ;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            fragments.remove(position);
        }

        public Fragment getFragment(){
            return fragments.get(curPos);
        }
    }

    @Override
    protected void onStop() {
        mThreadEnd = true;
        super.onStop();
    }
}
