package com.zhxh.xtouchsystem.touch;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created with Android Studio
 * Package name: com.zhxh.xtouchsystem
 * Author: zhxh
 * Date: 2015-7-18
 * Time: 17:20
 * To change this template use File | Settings | File and Code Templates.
 */
public class CButton extends android.support.v7.widget.AppCompatButton {
    public CButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("CButton---dispatchTouchEvent---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("CButton---dispatchTouchEvent---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("CButton---dispatchTouchEvent---UP");
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("CButton---onTouchEvent---DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("CButton---onTouchEvent---MOVE");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("CButton---onTouchEvent---UP");
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("zhxh", "CButton-" + "onDraw");
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("zhxh", "CButton-" + "onMeasure");

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d("zhxh", "CButton-" + "onLayout");

        super.onLayout(changed, left, top, right, bottom);
    }

}
