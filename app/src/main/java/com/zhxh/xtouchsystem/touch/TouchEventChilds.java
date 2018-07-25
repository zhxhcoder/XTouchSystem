package com.zhxh.xtouchsystem.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by zhxh on 15/2/5.
 */
public class TouchEventChilds extends LinearLayout {

    public static final String TAG = TouchEventChilds.class.getSimpleName();

    public TouchEventChilds(Context context) {
        super(context);
    }

    public TouchEventChilds(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e(TAG, "TouchEventChilds | dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
        return super.dispatchTouchEvent(event);
//        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.i(TAG, "TouchEventChilds | onInterceptTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "TouchEventChilds | onTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
        return super.onTouchEvent(event);
//        return true;
    }

}

