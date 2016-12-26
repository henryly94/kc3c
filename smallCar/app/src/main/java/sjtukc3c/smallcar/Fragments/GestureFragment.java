package sjtukc3c.smallcar.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

import sjtukc3c.smallcar.R;

public class GestureFragment extends MasterFragment {
    private int x1;
    private int x2;
    private int y1;
    private int y2;

    private TextView mResutText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("happy", "GestureSimpleFragment");
        View view = inflater.inflate(R.layout.fragment_master_gesture, container, false);
        mResutText = (TextView) view.findViewById(R.id.gesture_text);
        mResutText.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = (int) e.getX();
                        y1 = (int) e.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = (int) e.getX();
                        y2 = (int) e.getY();
                        if (y1 - y2 > 100) {
                            mResutText.setText("Forward");
                        } else if (y2 - y1 > 100) {
                            mResutText.setText("BACKWARD");
                        } else if (x1 - x2 > 100) {
                            mResutText.setText("LEFT");
                        } else if (x2 - x1 > 100) {
                            mResutText.setText("RIGHT");
                        } else if (((x1 - x2) < 10 || (x2 - x1) < 10) && ((y1 - y2) < 10 || (y2 - y1) < 10)) {
                            mResutText.setText("STOP");
                        }
                        break;
                }
                return true;
            }
        });
        return view;
    }


}
