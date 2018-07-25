package com.zhxh.xtouchsystem.touch.conflict;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhxh.xtouchsystem.R;

@SuppressLint("NewApi")
public class FirstFragment extends Fragment {
    private static final String TAG = "FirstFragment";
    private HorizontalListView horizontal;
    public CViewPager parent; // 由外面指定

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        //view.getParent().requestDisallowInterceptTouchEvent(true);
        horizontal = (HorizontalListView) view.findViewById(R.id.hscroll);
        horizontal.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    parent.requestDisallowInterceptTouchEvent(true);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });
        horizontal.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                parent.setLimitHeight(horizontal.getHeight());
                Log.v("yzy", "count-->" + horizontal.getChildCount());
                horizontal.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //horizontal=(HorizontalListView)view.findViewById(R.id.hscroll);
        horizontal.setAdapter(new BaseAdapter() {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = new TextView(FirstFragment.this.getActivity());

                TextView txtView = (TextView) convertView;
                txtView.setText("测试" + position);
                return convertView;
            }

            @Override
            public long getItemId(int arg0) {
                return 0;
            }

            @Override
            public Object getItem(int arg0) {
                return null;
            }

            @Override
            public int getCount() {
                return 20;
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) this.getActivity().getSystemService("window")).getDefaultDisplay().getMetrics(dm);

        parent.setLimitHeight(dm.heightPixels / 2);

    }

}
