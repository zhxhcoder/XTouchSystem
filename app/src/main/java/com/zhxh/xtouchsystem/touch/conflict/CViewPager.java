package com.zhxh.xtouchsystem.touch.conflict;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class CViewPager extends ViewPager {
    private static final String TAG = "CViewPager";
    private int limitHeight = 0;

    public void setLimitHeight(int height) {
        this.limitHeight = height;
    }

    public CViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
//    Log.v("yzy", "height-->"+limitHeight+",y-->"+arg0.getY());
//    if(arg0.getY()<limitHeight)
//      return false;
//    else
//      return true;
        Log.v("yzy", "pager--->" + super.onInterceptTouchEvent(arg0));
        return super.onInterceptTouchEvent(arg0);

    }

}
