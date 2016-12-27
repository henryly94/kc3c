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
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.IOException;
import java.net.ServerSocket;

import sjtukc3c.smallcar.Fragments.MasterFragment;
import sjtukc3c.smallcar.Fragments.RemoteStartFragment;
import sjtukc3c.smallcar.Modules.RemoteCommandManager;
import sjtukc3c.smallcar.Modules.SocketThreadMaster;
import sjtukc3c.smallcar.R;
import sjtukc3c.smallcar.Utils.CustomViewPager;

/**
 * Author: wenhao.zhu[weehowe.z@gmail.com]
 * Created on 1:25 AM 2016/12/14.
 */
public class MasterActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    // TODO: 2016/12/22 点多任务按键会崩溃，怀疑要在onPause处理，点back也会崩溃

    private Toolbar mToolbar;
    private Drawable oldBackground;
//    private ViewPager mPager;
    private CustomViewPager mPager;
    private PagerSlidingTabStrip mTab;
    private MyAdapter mAdapter;
    private SystemBarTintManager mTintManager;

    private Thread mThread;

    private SurfaceView mSurfaceView;
    private ImageView mBackButton;

    private int mPort = 15536;


    private RemoteCommandManager mCommandManger;
    private ServerSocket mServerSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        initView();
        initTool();
    }

    private void initView() {

        mBackButton = (ImageView) findViewById(R.id.btn_master_go_back);
        mBackButton.setOnClickListener(this);

        mSurfaceView = (SurfaceView) findViewById(R.id.sv_master);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mPager = (ViewPager) findViewById(R.id.pager);
        mPager = (CustomViewPager) findViewById(R.id.pager);

        setSupportActionBar(mToolbar);

        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        changeColor(ContextCompat.getColor(getBaseContext(), R.color.green));

        mTab = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(0);
        mTab.setViewPager(mPager);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        mPager.setPageMargin(pageMargin);


        mTab.setOnTabReselectedListener(new PagerSlidingTabStrip.OnTabReselectedListener() {
            @Override
            public void onTabReselected(int i) {
                Toast.makeText(MasterActivity.this, "Tab reselected: " + i, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initTool() {
        mCommandManger = new RemoteCommandManager(this);
        mThread = new SocketThreadMaster(mServerSocket, mSurfaceView, mPort, mCommandManger);
        Log.e("Lyy", "Even here");
        mThread.start();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.master, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_master_config:
                RemoteStartFragment.newInstance().show(getSupportFragmentManager(), "RemoteStartFragment");

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_master_go_back:
                finish();
                break;
            default:
                break;
        }
    }


    private class MyAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Normal", "Voice", "Gesture", "Gravity", "About"};

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            MasterFragment fragment = MasterFragment.newInstance(position);
            fragment.setRemoteCommandManager(mCommandManger);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.e("Lyy", "getPageTitle: " + TITLES[position]);
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }

    @Override
    protected void onDestroy() {
        ((SocketThreadMaster) mThread).stopit();
        try {
            if (mServerSocket != null && !mServerSocket.isClosed()) {
                mServerSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();

    }
}
