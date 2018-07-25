package com.zhxh.xtouchsystem.touch;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;


public class CLinearLayout extends LinearLayout {

    public CLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("zhxh", "【市长】-dispatchTouchEvent-任务<" + Util.actionToString(event.getAction()) + "> : 需要分派");
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean bo = false;
        Log.d("zhxh", "【市长】-onInterceptTouchEvent-任务<" + Util.actionToString(event.getAction()) + "> : 拦截吗？" + bo);
        return bo;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean bo = false;
        Log.d("zhxh", "【市长】-onTouchEvent-任务<" + Util.actionToString(ev.getAction()) + "> : 农民真没用，下次再也不找你了，我自己来尝试一下。能解决？" + bo);
        return bo;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}