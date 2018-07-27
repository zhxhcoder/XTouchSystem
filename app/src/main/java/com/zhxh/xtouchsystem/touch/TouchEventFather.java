package com.zhxh.xtouchsystem.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created with Android Studio
 * Package name: com.zhxh.xtouchsystem
 * Author: zhxh
 * Date: 15/2/5
 * Time: 10:13
 * To change this template use File | Settings | File and Code Templates.
 */
public class TouchEventFather extends LinearLayout {

    public static final String TAG = TouchEventFather.class.getSimpleName();

    public TouchEventFather(Context context) {
        super(context);
    }

    public TouchEventFather(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e(TAG, "TouchEventFather | dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
        return super.dispatchTouchEvent(event);
//        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.i(TAG, "TouchEventFather | onInterceptTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
//		return super.onInterceptTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "TouchEventFather | onTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
        return super.onTouchEvent(event);
//        return true;
    }
}
