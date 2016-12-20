package sjtukc3c.smallcar.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import sjtukc3c.smallcar.R;

/**
 * Created by Administrator on 2016/12/18.
 */
public class TestFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;
    private TextView mTextView;

    public static TestFragment newInstance(int p){
        TestFragment f = new TestFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, p);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        switch (position){
            case 0:
                rootView = inflater.inflate(R.layout.fragment_test_1, container, false);
                break;
            case 1:
                rootView = inflater.inflate(R.layout.fragment_test_2, container, false);
                break;
            case 2:
            default:
                rootView = inflater.inflate(R.layout.fragment_test_3, container, false);
                break;

        }
        ButterKnife.bind(this, rootView);

        return rootView;


//        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
